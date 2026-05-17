package com.ecommerce.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductsResponse {
    private List<TopProduct> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProduct {
        private String productId;
        private String productName;
        private String size;
        private String imageUrl;
        private BigDecimal price;
        private Integer stock;
        private String category;
        private Integer totalSold;
        private BigDecimal totalRevenue;
    }
}
