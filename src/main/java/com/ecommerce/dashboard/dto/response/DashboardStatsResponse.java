package com.ecommerce.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private StatisticCard totalPending;
    private StatisticCard totalSales;
    private StatisticCard totalOrders;
    private StatisticCard totalUsers;
}
