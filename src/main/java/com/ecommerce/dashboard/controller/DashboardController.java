package com.ecommerce.dashboard.controller;

import com.ecommerce.dashboard.dto.response.DashboardStatsResponse;
import com.ecommerce.dashboard.dto.response.SalesChartResponse;
import com.ecommerce.dashboard.dto.response.TopProductsResponse;
import com.ecommerce.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        log.info("GET /api/dashboard/stats");
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/sales-chart")
    public ResponseEntity<SalesChartResponse> getSalesChart(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        LocalDate now = LocalDate.now();
        if (year == null) year = now.getYear();
        if (month == null) month = now.getMonthValue();
        log.info("GET /api/dashboard/sales-chart?year={}&month={}", year, month);
        return ResponseEntity.ok(dashboardService.getSalesChart(year, month));
    }

    @GetMapping("/top-products")
    public ResponseEntity<TopProductsResponse> getTopProducts(@RequestParam(defaultValue = "10") Integer limit) {
        log.info("GET /api/dashboard/top-products?limit={}", limit);
        return ResponseEntity.ok(dashboardService.getTopProducts(limit));
    }

    @PostMapping("/cache/clear")
    public ResponseEntity<Map<String, String>> clearCache() {
        log.info("POST /api/dashboard/cache/clear");
        dashboardService.clearAllCache();
        return ResponseEntity.ok(Map.of("message", "All caches cleared successfully"));
    }
}
