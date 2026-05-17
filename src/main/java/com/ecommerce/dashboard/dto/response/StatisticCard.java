package com.ecommerce.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticCard {
    private String title;
    private Long value;
    private String formattedValue;
    private Double percentageChange;
    private String changeDirection;
    private String changeText;
    private String icon;
    private String iconColor;
}
