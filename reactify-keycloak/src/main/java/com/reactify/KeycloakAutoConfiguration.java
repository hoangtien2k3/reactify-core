package com.reactify;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Autoconfiguration for the reactify-oauth2-keycloak library.
 * <p>
 * Automatically scans and registers components in the <code>com.reactify</code> package,
 * enabling seamless integration with Spring Boot.
 * </p>
 *
 * <h3>Usage:</h3>
 * <pre>
 * Add the reactify-oauth2-keycloak dependency, and Spring Boot will configure its components automatically.
 * </pre>
 *
 * @author @hoangtien2k3
 * @since 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.reactify")
public class KeycloakAutoConfiguration {
}
