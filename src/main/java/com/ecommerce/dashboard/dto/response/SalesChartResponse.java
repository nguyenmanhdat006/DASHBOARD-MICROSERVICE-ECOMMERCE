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
public class SalesChartResponse {
    private List<SalesDataPoint> data;
    private String period;
    private BigDecimal totalSales;
    private BigDecimal averageSales;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesDataPoint {
        private String date;
        private String label;
        private BigDecimal sales;
        private Integer orderCount;
    }
}
