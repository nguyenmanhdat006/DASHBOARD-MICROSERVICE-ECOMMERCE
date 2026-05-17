package com.ecommerce.dashboard.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {

    @Qualifier("productServiceClient")
    private final WebClient productServiceClient;

    public ProductDetails getProductDetails(String productId) {
        try {
            return productServiceClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/products/{id}").build(productId))
                    .retrieve()
                    .bodyToMono(ProductDetails.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching product details {}", productId, e);
            return null;
        }
    }

    @lombok.Data
    public static class ProductDetails {
        private String productId;
        private String name;
        private BigDecimal price;
        private Integer stock;
        private String category;
        private String imageUrl;
    }
}
