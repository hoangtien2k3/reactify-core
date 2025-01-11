package com.reactify.test.client.properties;

import com.reactify.filter.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("baseCurrencyClientProperties")
@ConfigurationProperties(prefix = "client.base-currency", ignoreInvalidFields = true)
@AllArgsConstructor
public class BaseCurrencyClientProperties extends WebClientProperties {
}
