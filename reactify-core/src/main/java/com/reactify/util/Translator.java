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
package com.reactify.util;

import java.util.Locale;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Utility class for translating message codes to localized messages. Provides
 * methods to translate message codes to messages in different locales. Supports
 * synchronous and asynchronous translation.
 *
 * @author hoangtien2k3
 */
@Component
public class Translator {
    private static final Locale defaultLocale = Locale.forLanguageTag("vi");
    private static ReloadableResourceBundleMessageSource messageSource;

    @Qualifier("localeContextResolver2")
    private static LocaleContextResolver localeContextResolver;

    /**
     * Constructor for Translator. Initializes the message source and locale context
     * resolver.
     *
     * @param messageSource
     *            the message source for loading messages
     * @param localeContextResolver
     *            the locale context resolver for resolving locales
     */
    public Translator(
            ReloadableResourceBundleMessageSource messageSource, LocaleContextResolver localeContextResolver) {
        Translator.messageSource = messageSource;
        Translator.localeContextResolver = localeContextResolver;
    }

    /**
     * Translates a message code to a message in the default locale (Vietnamese).
     *
     * @param msgCode
     *            the message code to be translated
     * @param params
     *            optional parameters to be included in the message
     * @return the translated message in Vietnamese
     */
    public static String toLocaleVi(String msgCode, Object... params) {
        if (msgCode == null) {
            return "";
        }

        return messageSource.getMessage(msgCode, params, defaultLocale);
    }

    /**
     * Translates a message code to a message in the locale resolved from the given
     * ServerWebExchange. Falls back to the default locale if the exchange is null
     * or the locale cannot be resolved.
     *
     * @param msgCode
     *            the message code to be translated
     * @param exchange
     *            the ServerWebExchange from which to resolve the locale
     * @param params
     *            optional parameters to be included in the message
     * @return the translated message in the resolved locale
     */
    public static String toLocale(String msgCode, ServerWebExchange exchange, Object... params) {
        if (msgCode == null) {
            return "";
        }
        Locale locale;
        if (exchange == null) {
            locale = defaultLocale;
        } else {
            locale = localeContextResolver.resolveLocaleContext(exchange).getLocale();
        }
        return messageSource.getMessage(
                msgCode, params, locale == null ? Objects.requireNonNull(defaultLocale) : locale);
    }

    /**
     * Asynchronously translates a message code to a message in the locale resolved
     * from the given ServerWebExchange. Falls back to the default locale if the
     * exchange is null or the locale cannot be resolved.
     *
     * @param msgCode
     *            the message code to be translated
     * @param exchange
     *            the ServerWebExchange from which to resolve the locale
     * @param params
     *            optional parameters to be included in the message
     * @return a Mono containing the translated message in the resolved locale
     */
    public static Mono<String> toLocaleMono(String msgCode, ServerWebExchange exchange, Object... params) {
        if (msgCode == null) {
            return Mono.just("");
        }
        Locale locale;
        if (exchange == null) {
            locale = defaultLocale;
        } else {
            locale = localeContextResolver.resolveLocaleContext(exchange).getLocale();
        }
        return Mono.fromSupplier(() -> messageSource.getMessage(
                        msgCode, params, locale == null ? Objects.requireNonNull(defaultLocale) : locale))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Translates a message code to a message in the current locale. The current
     * locale is resolved from the LocaleContextHolder.
     *
     * @param msgCode
     *            the message code to be translated
     * @param params
     *            optional parameters to be included in the message
     * @return the translated message in the current locale
     */
    public static String toLocale(String msgCode, Object... params) {
        if (msgCode == null) {
            return "";
        }
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, params, locale);
    }

    /**
     * Translates a message code to a message in the current locale. The current
     * locale is resolved from the LocaleContextHolder.
     *
     * @param msgCode
     *            the message code to be translated
     * @return the translated message in the current locale
     */
    public static String toLocale(String msgCode) {
        return Translator.toLocale(msgCode, (Object) null);
    }

    /**
     * Asynchronously translates a message code to a message in the current locale.
     * The current locale is resolved from the LocaleContextHolder.
     *
     * @param msgCode
     *            the message code to be translated
     * @return a Mono containing the translated message in the current locale
     */
    public static Mono<String> toLocaleMono(String msgCode) {
        return Translator.toLocaleMono(msgCode, null);
    }
}
