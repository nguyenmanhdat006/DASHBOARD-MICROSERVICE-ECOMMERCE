package com.ecommerce.dashboard.service;

import com.ecommerce.dashboard.dto.response.DashboardStatsResponse;
import com.ecommerce.dashboard.dto.response.SalesChartResponse;
import com.ecommerce.dashboard.dto.response.TopProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final AggregationService aggregationService;

    public DashboardStatsResponse getDashboardStats() {
        return aggregationService.aggregateStats();
    }

    public SalesChartResponse getSalesChart(int year, int month) {
        return aggregationService.aggregateSalesChart(year, month);
    }

    public TopProductsResponse getTopProducts(int limit) {
        return aggregationService.aggregateTopProducts(limit);
    }

    @CacheEvict(value = "dashboardStats", allEntries = true)
    public void clearStatsCache() {
        log.info("Cleared dashboardStats cache");
    }

    @CacheEvict(allEntries = true, value = {"dashboardStats", "salesChart", "topProducts"})
    public void clearAllCache() {
        log.info("Cleared all caches");
    }
}
