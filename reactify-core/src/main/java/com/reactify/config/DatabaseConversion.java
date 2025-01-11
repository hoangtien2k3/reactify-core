/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reactify.config;

import io.r2dbc.spi.Blob;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The {@code DatabaseConversion} class provides utilities for converting
 * between database types and Java types in a reactive R2DBC context. It sets up
 * custom converters for various data types to facilitate seamless data mapping
 * between the database and application models.
 * </p>
 *
 * <p>
 * This class utilizes Spring Data R2DBC's conversion framework to define and
 * manage converters for types such as {@link java.time.Instant},
 * {@link java.util.UUID}, {@link java.time.Duration}, and others, ensuring that
 * the application can correctly handle these types when interacting with the
 * database.
 * </p>
 *
 * <p>
 * Additionally, it provides a method to retrieve a
 * {@link org.springframework.data.r2dbc.convert.MappingR2dbcConverter}, which
 * is used for converting between database rows and domain objects.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Component
public class DatabaseConversion {

    /**
     * Constructs a new instance of {@code DatabaseConversion}.
     */
    public DatabaseConversion() {}

    /**
     * <p>
     * Retrieves a
     * {@link org.springframework.data.r2dbc.convert.MappingR2dbcConverter}
     * configured with the necessary mapping context and custom conversions for the
     * application.
     * </p>
     *
     * @return a
     *         {@link org.springframework.data.r2dbc.convert.MappingR2dbcConverter}
     *         object configured for the application
     */
    public MappingR2dbcConverter getR2dbcConverter() {
        R2dbcMappingContext mappingContext = getR2dbcMappingContext();
        R2dbcCustomConversions r2dbcCustomConversions = getR2dbcCustomConversions();
        return new MappingR2dbcConverter(mappingContext, r2dbcCustomConversions);
    }

    /**
     * Creates and configures an instance of {@link R2dbcMappingContext}.
     *
     * @return a {@link R2dbcMappingContext} object that defines the mapping
     *         strategy for R2DBC.
     */
    private R2dbcMappingContext getR2dbcMappingContext() {
        NamingStrategy namingStrategy = DefaultNamingStrategy.INSTANCE;
        R2dbcCustomConversions r2dbcCustomConversions = getR2dbcCustomConversions();
        R2dbcMappingContext context = new R2dbcMappingContext(namingStrategy);
        context.setSimpleTypeHolder(r2dbcCustomConversions.getSimpleTypeHolder());
        return context;
    }

    /**
     * Creates a {@link R2dbcCustomConversions} object which contains a list of
     * custom converters for R2DBC.
     *
     * @return a {@link R2dbcCustomConversions} object configured with custom
     *         converters for specific types.
     */
    private R2dbcCustomConversions getR2dbcCustomConversions() {
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, getListConverters());
    }

    /**
     * Provides a list of custom converters for R2DBC operations.
     *
     * @return a {@link java.util.List} of custom converter instances.
     */
    public List<Object> getListConverters() {
        List<Object> converters = new ArrayList<>();
        converters.add(InstantWriteConverter.INSTANCE);
        converters.add(InstantReadConverter.INSTANCE);
        converters.add(BitSetReadConverter.INSTANCE);
        converters.add(DurationWriteConverter.INSTANCE);
        converters.add(DurationReadConverter.INSTANCE);
        converters.add(ZonedDateTimeReadConverter.INSTANCE);
        converters.add(ZonedDateTimeWriteConverter.INSTANCE);
        converters.add(BinaryToUUIDConverter.INSTANCE);
        converters.add(BlobToStringConverter.INSTANCE);
        converters.add(UUIDToBinaryConverter.INSTANCE);
        converters.add(LocalDateTimeToDateReadConverter.INSTANCE);
        return converters;
    }

    /**
     * Converter for writing {@link Instant} as {@link LocalDateTime}.
     */
    @WritingConverter
    public enum InstantWriteConverter implements Converter<Instant, LocalDateTime> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        public LocalDateTime convert(@NotNull Instant source) {
            return LocalDateTime.ofInstant(source, ZoneOffset.UTC);
        }
    }

    /**
     * Converter for reading {@link Blob} as {@link String}.
     */
    @ReadingConverter
    public enum BlobToStringConverter implements Converter<Blob, String> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public String convert(@NotNull Blob source) {
            try {
                return Mono.from(source.stream())
                        .map(bb -> StandardCharsets.UTF_8.decode(bb).toString())
                        .toFuture()
                        .get();
            } catch (Exception e) {
                log.error("Exception when read blob value", e);
                return null;
            }
        }
    }

    /**
     * Converter for writing {@link UUID} as {@code byte[]}.
     */
    @WritingConverter
    public enum UUIDToBinaryConverter implements Converter<UUID, byte[]> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public byte[] convert(UUID uuid) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        }
    }

    /**
     * Converter for reading {@code byte[]} as {@link UUID}.
     */
    @ReadingConverter
    public enum BinaryToUUIDConverter implements Converter<byte[], UUID> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public UUID convert(@NotNull byte[] source) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(source);
            long high = byteBuffer.getLong();
            long low = byteBuffer.getLong();
            return new UUID(high, low);
        }
    }

    /**
     * Converter for reading {@link LocalDateTime} as {@link Instant}.
     */
    @ReadingConverter
    public enum InstantReadConverter implements Converter<LocalDateTime, Instant> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public Instant convert(LocalDateTime localDateTime) {
            return localDateTime.toInstant(ZoneOffset.UTC);
        }
    }

    /**
     * Converter for reading {@link BitSet} as {@link Boolean}.
     */
    @ReadingConverter
    public enum BitSetReadConverter implements Converter<BitSet, Boolean> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public Boolean convert(BitSet bitSet) {
            return bitSet.get(0);
        }
    }

    /**
     * Converter for reading {@link LocalDateTime} as {@link ZonedDateTime}.
     */
    @ReadingConverter
    public enum ZonedDateTimeReadConverter implements Converter<LocalDateTime, ZonedDateTime> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public ZonedDateTime convert(@NotNull LocalDateTime localDateTime) {
            // Be aware - we are using the UTC timezone
            return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
        }
    }

    /**
     * Converter for writing {@link ZonedDateTime} as {@link LocalDateTime}.
     */
    @WritingConverter
    public enum ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, LocalDateTime> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public LocalDateTime convert(ZonedDateTime zonedDateTime) {
            return zonedDateTime.toLocalDateTime();
        }
    }

    /**
     * Converter for writing {@link Duration} as {@code Long}.
     */
    @WritingConverter
    public enum DurationWriteConverter implements Converter<Duration, Long> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public Long convert(@NotNull Duration source) {
            return source.toMillis();
        }
    }

    /**
     * Converter for reading {@link LocalDateTime} as {@link Date}.
     */
    @ReadingConverter
    public enum LocalDateTimeToDateReadConverter implements Converter<LocalDateTime, Date> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public Date convert(LocalDateTime localDateTime) {
            // Be aware - we are using the UTC timezone
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        }
    }

    /**
     * Converter for reading {@code Long} as {@link Duration}.
     */
    @ReadingConverter
    public enum DurationReadConverter implements Converter<Long, Duration> {
        /** The singleton instance of BinaryToUUIDConverter. */
        INSTANCE;

        @Override
        public Duration convert(@NotNull Long source) {
            return Duration.ofMillis(source);
        }
    }
}
