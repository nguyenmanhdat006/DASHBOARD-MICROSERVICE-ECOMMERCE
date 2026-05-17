package com.ecommerce.dashboard.service;

import com.ecommerce.dashboard.client.OrderServiceClient;
import com.ecommerce.dashboard.client.ProductServiceClient;
import com.ecommerce.dashboard.client.UserServiceClient;
import com.ecommerce.dashboard.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationService {

    private final OrderServiceClient orderServiceClient;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;
    private final StatisticsService statisticsService;

    @Cacheable("dashboardStats")
    public DashboardStatsResponse aggregateStats() {
        log.info("Aggregating dashboard statistics");

        Long totalPending = orderServiceClient.countPendingOrders();
        Long pendingYesterday = orderServiceClient.countPendingOrdersYesterday();
        StatisticCard pendingCard = statisticsService.buildStatCard(
                "Total Pending",
                totalPending,
                null,
                pendingYesterday,
                "from yesterday",
                "clock",
                "#FFE2E5"
        );

        BigDecimal totalSales = orderServiceClient.getTotalSales();
        BigDecimal salesYesterday = orderServiceClient.getTotalSalesYesterday();
        StatisticCard salesCard = statisticsService.buildStatCard(
                "Total Sales",
                null,
                totalSales,
                salesYesterday,
                "from yesterday",
                "trending-up",
                "#DCFCE7"
        );

        Long totalOrders = orderServiceClient.countTotalOrders();
        Long ordersLastWeek = orderServiceClient.countOrdersLastWeek();
        StatisticCard ordersCard = statisticsService.buildStatCard(
                "Total Orders",
                totalOrders,
                null,
                ordersLastWeek,
                "from past week",
                "package",
                "#FEF3C7"
        );

        Long totalUsers = userServiceClient.countTotalUsers();
        Long usersYesterday = userServiceClient.countUsersYesterday();
        StatisticCard usersCard = statisticsService.buildStatCard(
                "Total Users",
                totalUsers,
                null,
                usersYesterday,
                "from yesterday",
                "users",
                "#F3E8FF"
        );

        return DashboardStatsResponse.builder()
                .totalPending(pendingCard)
                .totalSales(salesCard)
                .totalOrders(ordersCard)
                .totalUsers(usersCard)
                .build();
    }

    @Cacheable(value = "salesChart", key = "#year + '-' + #month")
    public SalesChartResponse aggregateSalesChart(int year, int month) {
        log.info("Aggregating sales chart for {}/{}", year, month);

        List<SalesChartResponse.SalesDataPoint> data = orderServiceClient.getSalesByDate(year, month);

        BigDecimal totalSales = data.stream()
                .map(SalesChartResponse.SalesDataPoint::getSales)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageSales = data.isEmpty() ? BigDecimal.ZERO :
                totalSales.divide(new BigDecimal(data.size()), 2, RoundingMode.HALF_UP);

        YearMonth yearMonth = YearMonth.of(year, month);
        String period = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        return SalesChartResponse.builder()
                .data(data)
                .period(period)
                .totalSales(totalSales)
                .averageSales(averageSales)
                .build();
    }

    @Cacheable(value = "topProducts", key = "#limit")
    public TopProductsResponse aggregateTopProducts(int limit) {
        log.info("Aggregating top {} products", limit);

        List<TopProductsResponse.TopProduct> products = orderServiceClient.getTopProducts(limit);

        // try to enrich products with product details where available
        products.forEach(p -> {
            try {
                ProductServiceClient.ProductDetails details = productServiceClient.getProductDetails(p.getProductId());
                if (details != null) {
                    p.setImageUrl(details.getImageUrl());
                    p.setStock(details.getStock());
                    p.setCategory(details.getCategory());
                }
            } catch (Exception e) {
                log.warn("Failed to enrich product {}", p.getProductId());
            }
        });

        return TopProductsResponse.builder()
                .products(products)
                .build();
    }
}
