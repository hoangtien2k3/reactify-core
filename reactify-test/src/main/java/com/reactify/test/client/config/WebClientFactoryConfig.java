package com.reactify.test.client.config;

import com.reactify.WebClientFactory;
import com.reactify.test.client.properties.BaseCurrencyClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final BaseCurrencyClientProperties baseCurrencyClientProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext) {
        WebClientFactory factory = new WebClientFactory(applicationContext);
        factory.setWebClients(List.of(baseCurrencyClientProperties));
        return factory;
    }
}
