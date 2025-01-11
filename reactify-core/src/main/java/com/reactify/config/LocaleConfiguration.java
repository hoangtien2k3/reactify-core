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

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

/**
 * <p>
 * LocaleConfiguration class is responsible for configuring locale-related
 * settings in the Spring application. It defines beans for handling locale
 * resolution and message source for internationalization.
 * </p>
 *
 * <p>
 * This configuration class provides a
 * {@link org.springframework.web.server.i18n.LocaleContextResolver} that
 * determines the current locale based on the Accept-Language header of incoming
 * requests, and a message source for loading localized messages from resource
 * bundles.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
public class LocaleConfiguration {

    /**
     * Constructs a new instance of {@code LocaleConfiguration}.
     */
    public LocaleConfiguration() {}

    /**
     * <p>
     * Creates a {@link org.springframework.web.server.i18n.LocaleContextResolver}
     * bean that resolves the locale from the HTTP Accept-Language header. The
     * default locale is set to Vietnamese ("vi").
     * </p>
     *
     * @return a {@link org.springframework.web.server.i18n.LocaleContextResolver}
     *         object configured to resolve the locale based on the Accept-Language
     *         header and defaults to Vietnamese.
     */
    @Bean("localeContextResolver2")
    public LocaleContextResolver localeContextResolver() {
        var resolver = new AcceptHeaderLocaleContextResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("vi"));
        return resolver;
    }

    /**
     * <p>
     * Creates a
     * {@link org.springframework.context.support.ReloadableResourceBundleMessageSource}
     * bean for loading message resources from property files. The message source is
     * configured to use UTF-8 encoding and loads messages from the
     * "classpath:/i18n/messages" resource bundle.
     * </p>
     *
     * @return a
     *         {@link org.springframework.context.support.ReloadableResourceBundleMessageSource}
     *         object configured for loading localized messages.
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource rs = new ReloadableResourceBundleMessageSource();
        rs.setDefaultEncoding("UTF-8");
        rs.setBasenames("classpath:/i18n/messages");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }
}
