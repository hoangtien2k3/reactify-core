/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến
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
package com.reactify.annotations.logging;

import static com.reactify.constants.Constants.MAX_BYTE;

import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.logging.LogField;
import com.reactify.model.logging.LoggerDTO;
import com.reactify.util.DataUtil;
import com.reactify.util.RequestUtils;
import com.reactify.util.TruncateUtils;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * LoggerSchedule class is responsible for scheduling the logging of requests
 * and responses. It retrieves log records from the LoggerQueue and processes
 * them at a fixed interval defined by the @Scheduled annotation.
 *
 * <p>
 * This class is marked as a Spring configuration and uses
 * Lombok's @RequiredArgsConstructor for automatic constructor injection,
 * ensuring that all required fields are initialized appropriately.
 * </p>
 *
 * <p>
 * Additionally, it uses Lombok's annotations for automatic constructor
 * injection and logging. The logging is performed using a dedicated performance
 * logger.
 * </p>
 *
 * <p>
 * Usage: This class is automatically instantiated by the Spring framework, and
 * the scheduleSaveLogClick method is invoked every 3 seconds. Ensure that
 * LoggerQueue is populated with LoggerDTO records before the scheduled task
 * runs.
 * </p>
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * // No manual instantiation is required as Spring manages it
 * // Just ensure LoggerQueue has records
 * </pre>
 *
 * @author hoangtien2k3
 */
@Configuration
@Slf4j
public class LoggerSchedule {

    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");

    /**
     * <p>
     * scheduleSaveLogClick method is called at a fixed interval (3000 milliseconds)
     * to process log records from the LoggerQueue. It counts the number of
     * successful and failed processing attempts and logs errors when they occur.
     * </p>
     *
     * <p>
     * It retrieves records from LoggerQueue, processes each record, and resets the
     * count after processing.
     * </p>
     */
    @Scheduled(fixedDelay = 3000)
    public void scheduleSaveLogClick() {
        long analyId = System.currentTimeMillis();
        AtomicInteger numSuccess = new AtomicInteger(0);
        AtomicInteger numFalse = new AtomicInteger(0);

        List<LoggerDTO> records = LoggerQueue.getInstance().getRecords();
        records.parallelStream().forEach(record -> {
            try {
                process(record);
                numSuccess.incrementAndGet();
            } catch (Exception e) {
                numFalse.incrementAndGet();
                log.error("Error while handling record queue: {}", e.getMessage(), e);
            }
        });

        log.info("Log Process - ID: {}, Success: {}, Failed: {}", analyId, numSuccess.get(), numFalse.get());
        LoggerQueue.getInstance().resetCount();
    }

    /**
     * <p>
     * Processes a single LoggerDTO record, extracting relevant information such as
     * trace ID, IP address, request ID, input arguments, and response. It logs this
     * information using logInfo.
     * </p>
     *
     * @param record
     *            The LoggerDTO record to process.
     */
    private void process(LoggerDTO record) {
        if (record == null) {
            return;
        }
        String traceId = !DataUtil.isNullOrEmpty(record.newSpan().context().traceIdString())
                ? record.newSpan().context().traceIdString()
                : "";
        String ipAddress = null;
        String requestId = null;
        ServerWebExchange exchange = Optional.ofNullable(record.contextRef().get())
                .filter(ctx -> ctx.hasKey(ServerWebExchange.class))
                .map(ctx -> ctx.get(ServerWebExchange.class))
                .orElse(null);
        if (exchange != null) {
            ServerHttpRequest request = exchange.getRequest();
            ipAddress = RequestUtils.getIpAddress(request);
            requestId = Optional.ofNullable(request.getHeaders().getFirst("Request-Id"))
                    .filter(s -> !DataUtil.isNullOrEmpty(s))
                    .orElse(null);
        }

        String inputs = null;
        try {
            if (record.args() != null) {
                inputs = ObjectMapperFactory.getInstance().writeValueAsString(getAgrs(record.args()));
            }
        } catch (Exception ex) {
            log.error("Error while handle record queue: {}", ex.getMessage());
        }

        String resStr = null;
        try {
            if (record.response() instanceof Optional<?> output) {
                if (output.isPresent()) {
                    resStr = ObjectMapperFactory.getInstance().writeValueAsString(output.get());
                }
            } else {
                if (record.response() != null) {
                    resStr = ObjectMapperFactory.getInstance().writeValueAsString(record.response());
                }
            }
        } catch (Exception ex) {
            log.error("Error while handle record queue: {}", ex.getMessage());
        }

        try {
            inputs = TruncateUtils.truncate(inputs, MAX_BYTE);
            resStr = TruncateUtils.truncate(resStr, MAX_BYTE);
        } catch (Exception ex) {
            log.error("Truncate input/output error ", ex);
        }

        logInfo(new LogField(
                traceId,
                requestId,
                record.service(),
                record.endTime() - record.startTime(),
                record.logType(),
                record.actionType(),
                record.startTime(),
                record.endTime(),
                ipAddress,
                record.title(),
                inputs,
                resStr,
                record.result()));
    }

    /**
     * <p>
     * Logs the information in a structured format using the perfLogger.
     * </p>
     *
     * @param logField
     *            The log data to be written.
     */
    private void logInfo(LogField logField) {
        try {
            logPerf.info(ObjectMapperFactory.getInstance().writeValueAsString(logField));
        } catch (Exception ex) {
            log.error("Error while handle record queue: {}", ex.getMessage());
        }
    }

    /**
     * <p>
     * Filters out Mono and ServerWebExchange instances from the input arguments and
     * returns a list of valid arguments.
     * </p>
     *
     * @param args
     *            The original array of arguments.
     * @return A list of non-Mono and non-ServerWebExchange arguments.
     */
    private List<Object> getAgrs(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> !(arg instanceof ServerWebExchange))
                .map(arg -> arg instanceof Mono ? ((Mono<?>) arg).block() : arg)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
