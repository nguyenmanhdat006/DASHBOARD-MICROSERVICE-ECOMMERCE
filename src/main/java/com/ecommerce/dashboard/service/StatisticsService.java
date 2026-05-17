package com.ecommerce.dashboard.service;

import com.ecommerce.dashboard.dto.response.StatisticCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@Slf4j
public class StatisticsService {

    public StatisticCard buildStatCard(String title, Long value, BigDecimal salesValue,
                                       Object compareValue, String compareText,
                                       String icon, String iconColor) {
        StatisticCard.StatisticCardBuilder builder = StatisticCard.builder()
                .title(title)
                .icon(icon)
                .iconColor(iconColor)
                .changeText(compareText);

        if (salesValue != null) {
            builder.formattedValue(formatCurrency(salesValue));
        } else if (value != null) {
            builder.formattedValue(formatNumber(value));
            builder.value(value);
        }

        double pct = calculatePercentageChange(compareValue, value, salesValue);
        builder.percentageChange(pct);
        builder.changeDirection(pct >= 0 ? "up" : "down");

        return builder.build();
    }

    public String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0";
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        return fmt.format(amount);
    }

    public String formatNumber(Long number) {
        if (number == null) return "0";
        return NumberFormat.getInstance(Locale.US).format(number);
    }

    private double calculatePercentageChange(Object compareValue, Long numberValue, BigDecimal salesValue) {
        try {
            double previous = 0.0;
            double current = 0.0;
            if (salesValue != null) {
                current = salesValue.doubleValue();
                if (compareValue instanceof BigDecimal) previous = ((BigDecimal) compareValue).doubleValue();
            } else if (numberValue != null) {
                current = numberValue.doubleValue();
                if (compareValue instanceof Number) previous = ((Number) compareValue).doubleValue();
            }
            if (previous == 0) return current == 0 ? 0.0 : 100.0;
            return ((current - previous) / previous) * 100.0;
        } catch (Exception e) {
            log.warn("Failed to calculate percentage change", e);
            return 0.0;
        }
    }
}
