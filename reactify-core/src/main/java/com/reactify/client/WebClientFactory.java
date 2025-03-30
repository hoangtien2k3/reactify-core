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
package com.reactify.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.reactify.client.properties.WebClientProperties;
import com.reactify.constants.Constants;
import com.reactify.filter.properties.ProxyProperties;
import com.reactify.filter.webclient.WebClientLoggingFilter;
import com.reactify.filter.webclient.WebClientMonitoringFilter;
import com.reactify.filter.webclient.WebClientRetryHandler;
import com.reactify.util.DataUtil;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.ProxyProvider;

/**
 * <p>
 * The {@code WebClientFactory} class is responsible for creating and managing
 * instances of
 * {@link org.springframework.web.reactive.function.client.WebClient} configured
 * with various properties. It integrates with Spring's application context to
 * register the created clients as beans for dependency injection. This class
 * supports OAuth2 authentication, connection pooling, and various customization
 * options such as logging, retry handling, and proxy configuration.
 * </p>
 *
 * <p>
 * The class implements the
 * {@link org.springframework.beans.factory.InitializingBean} interface, which
 * triggers the initialization of web clients after the bean properties have
 * been set. Each web client is created based on the specified
 * {@link com.reactify.client.properties.WebClientProperties}.
 * </p>
 *
 * <p>
 * The factory allows dynamic creation of multiple
 * {@link org.springframework.web.reactive.function.client.WebClient} instances
 * based on provided configurations, enabling flexible and efficient HTTP
 * communication in reactive applications.
 * </p>
 *
 * @author hoangtien2k3
 */
public class WebClientFactory implements InitializingBean {

    /**
     * A static logger instance for logging messages related to WebClientFactory.
     */
    private static final Logger log = LoggerFactory.getLogger(WebClientFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ReactiveOAuth2AuthorizedClientManager authorizedClientManager;

    /**
     * A list of {@code WebClientProperties} containing configuration file
     * application.properties or application.yml
     */
    private final List<WebClientProperties> webClients;

    /**
     * Creates an instance of {@code WebClientFactory} with a specified list of
     * {@code WebClientProperties}. This constructor allows pre-configuring the
     * factory with properties that define how {@code WebClient} instances will be
     * created.
     *
     * @param webClients
     *            a list of {@code WebClientProperties} containing configuration
     *            details for creating {@code WebClient} instances
     */
    public WebClientFactory(List<WebClientProperties> webClients) {
        this.webClients = webClients;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method initializes the web clients by calling
     * {@link #initWebClients(List)} with the configured web client properties.
     * </p>
     */
    @Override
    public void afterPropertiesSet() {
        initWebClients(webClients);
    }

    /**
     * <p>
     * Initializes web clients based on the provided list of
     * {@link com.reactify.client.properties.WebClientProperties}. Each client is
     * created and registered as a singleton bean in the application context.
     * </p>
     *
     * @param webClients
     *            a {@link java.util.List} of
     *            {@link com.reactify.client.properties.WebClientProperties} objects
     *            containing configuration for each web client
     */
    public void initWebClients(List<WebClientProperties> webClients) {
        final ConfigurableListableBeanFactory beanFactory =
                ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        for (WebClientProperties webClientProperties : webClients) {
            var webClient = createNewClient(webClientProperties);
            if (webClient != null) {
                beanFactory.registerSingleton(webClientProperties.getName(), webClient);
            }
        }
    }

    /**
     * <p>
     * Creates a new instance of
     * {@link org.springframework.web.reactive.function.client.WebClient} using the
     * provided {@link com.reactify.client.properties.WebClientProperties}. The
     * client is configured with connection pooling, timeout settings, and
     * additional filters based on the properties specified.
     * </p>
     *
     * @param webClientProperties
     *            a {@link com.reactify.client.properties.WebClientProperties}
     *            object containing configuration for the web client
     * @return a {@link org.springframework.web.reactive.function.client.WebClient}
     *         object configured based on the given properties, or {@code null} if
     *         the properties are invalid
     */
    public WebClient createNewClient(WebClientProperties webClientProperties) {
        if (!isValidProperties(webClientProperties)) {
            log.error("Failed to setup a webClientProperties {}", webClientProperties.getName());
            return null;
        }
        ConnectionProvider connectionProvider = ConnectionProvider.builder(webClientProperties.getName() + "Pool")
                .maxConnections(webClientProperties.getPool().getMaxSize())
                .pendingAcquireMaxCount(webClientProperties.getPool().getMaxPendingAcquire())
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        webClientProperties.getTimeout().getConnection())
                .responseTimeout(
                        Duration.ofMillis(webClientProperties.getTimeout().getRead()))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.registerDefaults(true);
                    configurer.defaultCodecs().maxInMemorySize(64 * 1024 * 1024);
                })
                .build();

        Builder exchangeStrategies =
                WebClient.builder().baseUrl(webClientProperties.getAddress()).exchangeStrategies(strategies);
        if (!DataUtil.isNullOrEmpty(webClientProperties.getUsername())) {
            exchangeStrategies.defaultHeader(
                    Constants.Security.AUTHORIZATION,
                    Constants.Security.BEARER + " "
                            + Base64.getEncoder()
                                    .encodeToString((webClientProperties.getUsername() + ":"
                                                    + webClientProperties.getPassword())
                                            .getBytes(UTF_8)));
        }
        if (webClientProperties.getLog().isEnable()) {
            exchangeStrategies.filter(
                    new WebClientLoggingFilter(webClientProperties.getLog().getObfuscateHeaders()));
        }
        if (webClientProperties.getRetry().isEnable()) {
            exchangeStrategies.filter(new WebClientRetryHandler(webClientProperties.getRetry()));
        }
        if (webClientProperties.getMonitoring().isEnable()) {
            exchangeStrategies.filter(new WebClientMonitoringFilter(
                    webClientProperties.getMonitoring().getMeterRegistry()));
        }
        if (webClientProperties.getProxy().isEnable()) {
            httpClient = configProxy(httpClient, webClientProperties.getProxy());
        }
        if (webClientProperties.isInternalOauth()) {
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                    new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
            oauth2.setDefaultClientRegistrationId(Constants.Security.DEFAULT_REGISTRATION_ID);
            exchangeStrategies.filter(oauth2);
        }

        List<ExchangeFilterFunction> customFilters = webClientProperties.getCustomFilters();
        if (customFilters != null) {
            for (ExchangeFilterFunction filter : customFilters) {
                exchangeStrategies.filter(filter);
            }
        }
        log.info("Success setup client properties {}", webClientProperties.getName());
        var clientConnector = new ReactorClientHttpConnector(httpClient);

        return exchangeStrategies.clientConnector(clientConnector).build();
    }

    /**
     * <p>
     * Configures the HTTP client to use a proxy if specified in the
     * {@link ProxyProperties}. This method sets up the HTTP and HTTPS proxies with
     * the given host and port.
     * </p>
     *
     * @param httpClient
     *            the original {@link HttpClient} to configure
     * @param proxyConfig
     *            the {@link ProxyProperties} containing proxy configuration
     * @return a {@link HttpClient} object configured with proxy settings
     */
    private HttpClient configProxy(HttpClient httpClient, ProxyProperties proxyConfig) {
        var httpHost = proxyConfig.getHttpHost();
        var httpPort = proxyConfig.getHttpPort();
        var httpsHost = proxyConfig.getHttpsHost();
        var httpsPort = proxyConfig.getHttpsPort();
        if (!DataUtil.isNullOrEmpty(httpHost) && !DataUtil.isNullOrEmpty(httpPort)) {
            httpClient = httpClient.proxy(
                    proxy -> proxy.type(ProxyProvider.Proxy.HTTP).host(httpHost).port(httpPort));
        }
        if (!DataUtil.isNullOrEmpty(httpsHost) && !DataUtil.isNullOrEmpty(httpsPort)) {
            SslContext sslContext;
            try {
                sslContext = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
            } catch (Exception ex) {
                return httpClient;
            }
            httpClient = httpClient
                    .proxy(proxy ->
                            proxy.type(ProxyProvider.Proxy.HTTP).host(httpsHost).port(httpsPort))
                    .secure(t -> t.sslContext(sslContext));
        }
        return httpClient;
    }

    /**
     * <p>
     * Validates the provided {@link WebClientProperties}. This method checks
     * whether the required fields, such as the name and address, are properly set.
     * </p>
     *
     * @param webClientProperties
     *            the {@link WebClientProperties} to validate
     * @return {@code true} if the properties are valid; {@code false} otherwise
     */
    private boolean isValidProperties(WebClientProperties webClientProperties) {
        return !DataUtil.isNullOrEmpty(webClientProperties.getName())
                && !DataUtil.isNullOrEmpty(webClientProperties.getAddress());
    }
}
