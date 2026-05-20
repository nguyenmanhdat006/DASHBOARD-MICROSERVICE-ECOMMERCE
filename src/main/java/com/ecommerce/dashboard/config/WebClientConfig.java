package com.ecommerce.dashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.order.url}")
    private String orderServiceUrl;

    @Value("${services.user.url}")
    private String userServiceUrl;

    @Value("${services.product.url}")
    private String productServiceUrl;

    @Value("${services.payment.url}")
    private String paymentServiceUrl;

    @Bean(name = "orderWebClient")
    public WebClient orderWebClient() {
        return WebClient.builder()
                .baseUrl(orderServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "userWebClient")
    public WebClient userWebClient() {
        return WebClient.builder()
                .baseUrl(userServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "productWebClient")
    public WebClient productWebClient() {
        return WebClient.builder()
                .baseUrl(productServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "paymentWebClient")
    public WebClient paymentWebClient() {
        return WebClient.builder()
                .baseUrl(paymentServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
