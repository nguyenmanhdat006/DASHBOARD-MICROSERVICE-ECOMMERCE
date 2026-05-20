package com.ecommerce.dashboard.client;

import com.ecommerce.dashboard.dto.response.SalesChartResponse;
import com.ecommerce.dashboard.dto.response.TopProductsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class OrderServiceClient {

    private final WebClient orderServiceClient;

    public OrderServiceClient(@Qualifier("orderWebClient") WebClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    public Long countPendingOrders() {
        try {
            return orderServiceClient.get()
                    .uri("/api/orders/stats/pending-count")
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching pending orders count", e);
            return 0L;
        }
    }

    public Long countPendingOrdersYesterday() {
        try {
            return orderServiceClient.get()
                    .uri("/api/orders/stats/pending-count-yesterday")
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching yesterday pending orders", e);
            return 0L;
        }
    }

    public BigDecimal getTotalSales() {
        try {
            return orderServiceClient.get()
                    .uri("/api/orders/stats/total-sales")
                    .retrieve()
                    .bodyToMono(BigDecimal.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching total sales", e);
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalSalesYesterday() {
        try {
            return orderServiceClient.get()
                    .uri("/api/orders/stats/total-sales-yesterday")
                    .retrieve()
                    .bodyToMono(BigDecimal.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching yesterday total sales", e);
            return BigDecimal.ZERO;
        }
    }

    public Long countTotalOrders() {
        try {
            return orderServiceClient.get()
                    .uri("/api/orders/stats/total-count")
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching total orders", e);
            return 0L;
        }
    }

    public Long countOrdersLastWeek() {
        try {
            return orderServiceClient.get()
                    .uri("/api/orders/stats/count-last-week")
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching last week orders", e);
            return 0L;
        }
    }

    public List<SalesChartResponse.SalesDataPoint> getSalesByDate(int year, int month) {
        try {
            return orderServiceClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/orders/stats/sales-by-date")
                            .queryParam("year", year)
                            .queryParam("month", month)
                            .build())
                    .retrieve()
                    .bodyToFlux(SalesChartResponse.SalesDataPoint.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("Error fetching sales by date", e);
            return List.of();
        }
    }

    public List<TopProductsResponse.TopProduct> getTopProducts(int limit) {
        try {
            return orderServiceClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/orders/stats/top-products")
                            .queryParam("limit", limit)
                            .build())
                    .retrieve()
                    .bodyToFlux(TopProductsResponse.TopProduct.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("Error fetching top products", e);
            return List.of();
        }
    }
}
