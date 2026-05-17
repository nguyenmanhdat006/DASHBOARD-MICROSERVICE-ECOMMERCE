# DASHBOARD SERVICE - SEPARATE MICROSERVICE

**Dashboard Service - Microservice độc lập để quản lý admin dashboard**

---

## 🎯 TỔNG QUAN

### **Dashboard Service:**
- **Port:** 8089
- **Database:** dashboard_db (PostgreSQL - port 5440)
- **Vai trò:** Aggregate data từ các services khác
- **Chức năng:** Cung cấp thống kê, biểu đồ cho admin dashboard

---

## 📊 KIẾN TRÚC

```
┌─────────────────────────────────────────────────────────┐
│              DASHBOARD SERVICE (8089)                   │
│                                                         │
│  - Aggregate data từ Order, User, Product services     │
│  - Cache statistics                                     │
│  - Provide admin dashboard APIs                         │
└─────────────────────────────────────────────────────────┘
         ↓ REST API Calls
┌────────┬───────────┬────────────┬──────────────┐
│ Order  │ User      │ Product    │ Payment      │
│Service │Service    │Service     │Service       │
│ 8084   │ 8081      │ 8082       │ 8085         │
└────────┴───────────┴────────────┴──────────────┘
```

---

## 📁 PROJECT STRUCTURE

```
dashboard-service/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/dashboardservice/
│   │   │   ├── DashboardServiceApplication.java
│   │   │   ├── config/
│   │   │   │   ├── WebClientConfig.java
│   │   │   │   └── CacheConfig.java
│   │   │   ├── controller/
│   │   │   │   └── DashboardController.java
│   │   │   ├── service/
│   │   │   │   ├── DashboardService.java
│   │   │   │   ├── StatisticsService.java
│   │   │   │   └── AggregationService.java
│   │   │   ├── client/
│   │   │   │   ├── OrderServiceClient.java
│   │   │   │   ├── UserServiceClient.java
│   │   │   │   ├── ProductServiceClient.java
│   │   │   │   └── PaymentServiceClient.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   │       ├── DashboardStatsResponse.java
│   │   │   │       ├── SalesChartResponse.java
│   │   │   │       ├── TopProductsResponse.java
│   │   │   │       └── StatisticCard.java
│   │   │   ├── entity/
│   │   │   │   └── DashboardCache.java
│   │   │   └── repository/
│   │   │       └── DashboardCacheRepository.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
└── README.md

Total: ~25 files
Time: 3-4 hours
```

---

## 🔧 PHASE 1: Setup Project (15 min)

### **File 1:** `pom.xml`

**Copilot Prompt:**
```xml
Create pom.xml for Dashboard Service microservice with dependencies:
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- PostgreSQL Driver
- Spring Boot Starter WebFlux (for WebClient)
- Spring Cloud Eureka Client
- Spring Boot Starter Cache
- Caffeine Cache
- Lombok
- MapStruct
```

**Expected Code:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <groupId>com.ecommerce</groupId>
    <artifactId>dashboard-service</artifactId>
    <version>1.0.0</version>
    <name>Dashboard Service</name>
    
    <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        
        <!-- WebClient -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        
        <!-- Cache -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        
        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>caffeine</artifactId>
        </dependency>
        
        <!-- Eureka Client -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        
        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>1.5.5.Final</version>
        </dependency>
        
        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

### **File 2:** `application.yml`

**Copilot Prompt:**
```yaml
Create application.yml for Dashboard Service:
- Port 8089
- Database dashboard_db on port 5440
- Eureka client config
- Service URLs for Order, User, Product, Payment services
- Cache config with TTL 5 minutes
```

**Expected Code:**
```yaml
server:
  port: 8089

spring:
  application:
    name: dashboard-service
  
  datasource:
    url: jdbc:postgresql://localhost:5440/dashboard_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=100,expireAfterWrite=300s

# Service URLs (hoặc dùng Eureka service discovery)
services:
  order:
    url: http://localhost:8084
  user:
    url: http://localhost:8081
  product:
    url: http://localhost:8082
  payment:
    url: http://localhost:8085

# Cache TTL (seconds)
cache:
  stats-ttl: 300        # 5 minutes
  chart-ttl: 600        # 10 minutes
  products-ttl: 300     # 5 minutes

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true

logging:
  level:
    com.ecommerce.dashboardservice: DEBUG
```

---

## 📝 PHASE 2: DTOs (20 min)

### **File 3:** `dto/response/StatisticCard.java`

**Copilot Prompt:**
```java
Create StatisticCard DTO with fields:
- title (String)
- value (Long, nullable)
- formattedValue (String)
- percentageChange (Double)
- changeDirection (String: "up" or "down")
- changeText (String)
- icon (String)
- iconColor (String)
Add Lombok annotations
```

---

### **File 4:** `dto/response/DashboardStatsResponse.java`

**Copilot Prompt:**
```java
Create DashboardStatsResponse with 4 StatisticCard fields:
- totalPending
- totalSales
- totalOrders
- totalUsers
Add Lombok @Data, @Builder
```

---

### **File 5:** `dto/response/SalesChartResponse.java`

**Copilot Prompt:**
```java
Create SalesChartResponse with:
- data (List<SalesDataPoint>)
- period (String)
- totalSales (BigDecimal)
- averageSales (BigDecimal)

Inner class SalesDataPoint:
- date (String)
- label (String)
- sales (BigDecimal)
- orderCount (Integer)

Add Lombok annotations
```

---

### **File 6:** `dto/response/TopProductsResponse.java`

**Copilot Prompt:**
```java
Create TopProductsResponse with:
- products (List<TopProduct>)

Inner class TopProduct:
- productId, productName, size, imageUrl
- price (BigDecimal)
- stock (Integer)
- category (String)
- totalSold (Integer)
- totalRevenue (BigDecimal)

Add Lombok annotations
```

---

## 🔌 PHASE 3: Service Clients (45 min)

### **File 7:** `config/WebClientConfig.java`

**Copilot Prompt:**
```java
Create WebClientConfig with @Configuration:
Create 4 WebClient beans:
- orderServiceClient (baseUrl from services.order.url)
- userServiceClient (baseUrl from services.user.url)
- productServiceClient (baseUrl from services.product.url)
- paymentServiceClient (baseUrl from services.payment.url)

Each with default header Content-Type: application/json
Use @Value to inject URLs from application.yml
```

**Expected Code:**
```java
package com.ecommerce.dashboardservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Value("${services.order.url}")
    private String orderServiceUrl;
    
    @Value("${services.user.url}")
    private String userServiceUrl;
    
    @Value("${services.product.url}")
    private String productServiceUrl;
    
    @Value("${services.payment.url}")
    private String paymentServiceUrl;
    
    @Bean
    public WebClient orderServiceClient() {
        return WebClient.builder()
            .baseUrl(orderServiceUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
    
    @Bean
    public WebClient userServiceClient() {
        return WebClient.builder()
            .baseUrl(userServiceUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
    
    @Bean
    public WebClient productServiceClient() {
        return WebClient.builder()
            .baseUrl(productServiceUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
    
    @Bean
    public WebClient paymentServiceClient() {
        return WebClient.builder()
            .baseUrl(paymentServiceUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
```

---

### **File 8:** `client/OrderServiceClient.java`

**Copilot Prompt:**
```java
Create OrderServiceClient with @Component:

Methods:
1. Long countPendingOrders()
   - GET /api/orders/stats/pending-count
   - Return Long

2. Long countPendingOrdersYesterday()
   - GET /api/orders/stats/pending-count-yesterday
   - Return Long

3. BigDecimal getTotalSales()
   - GET /api/orders/stats/total-sales
   - Return BigDecimal

4. BigDecimal getTotalSalesYesterday()
   - GET /api/orders/stats/total-sales-yesterday
   - Return BigDecimal

5. Long countTotalOrders()
   - GET /api/orders/stats/total-count
   - Return Long

6. Long countOrdersLastWeek()
   - GET /api/orders/stats/count-last-week
   - Return Long

7. List<SalesDataPoint> getSalesByDate(int year, int month)
   - GET /api/orders/stats/sales-by-date?year={year}&month={month}
   - Return List<SalesDataPoint>

8. List<TopProduct> getTopProducts(int limit)
   - GET /api/orders/stats/top-products?limit={limit}
   - Return List<TopProduct>

Use WebClient with @Qualifier("orderServiceClient")
Add error handling with try-catch
Add logging with @Slf4j
```

**Expected Code:**
```java
package com.ecommerce.dashboardservice.client;

import com.ecommerce.dashboardservice.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderServiceClient {
    
    @Qualifier("orderServiceClient")
    private final WebClient orderServiceClient;
    
    public Long countPendingOrders() {
        log.info("Fetching pending orders count from Order Service");
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
        log.info("Fetching yesterday's pending orders count");
        try {
            return orderServiceClient.get()
                .uri("/api/orders/stats/pending-count-yesterday")
                .retrieve()
                .bodyToMono(Long.class)
                .block();
        } catch (Exception e) {
            log.error("Error fetching yesterday's pending orders", e);
            return 0L;
        }
    }
    
    public BigDecimal getTotalSales() {
        log.info("Fetching total sales from Order Service");
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
        log.info("Fetching yesterday's total sales");
        try {
            return orderServiceClient.get()
                .uri("/api/orders/stats/total-sales-yesterday")
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();
        } catch (Exception e) {
            log.error("Error fetching yesterday's sales", e);
            return BigDecimal.ZERO;
        }
    }
    
    public Long countTotalOrders() {
        log.info("Fetching total orders count");
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
        log.info("Fetching last week's orders count");
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
        log.info("Fetching sales by date for {}/{}", year, month);
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
        log.info("Fetching top {} products", limit);
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
```

---

### **File 9:** `client/UserServiceClient.java`

**Copilot Prompt:**
```java
Create UserServiceClient with @Component:

Methods:
1. Long countTotalUsers()
   - GET /api/users/stats/total-count
   
2. Long countUsersYesterday()
   - GET /api/users/stats/count-yesterday

Use WebClient with @Qualifier("userServiceClient")
Add error handling and logging
```

---

### **File 10:** `client/ProductServiceClient.java`

**Copilot Prompt:**
```java
Create ProductServiceClient with @Component:

Methods:
1. ProductDetails getProductDetails(String productId)
   - GET /api/products/{productId}
   - Return ProductDetails (productId, name, price, stock, category, imageUrl)

Use WebClient with @Qualifier("productServiceClient")
Add error handling and logging
```

---

## 💼 PHASE 4: Services (60 min)

### **File 11:** `config/CacheConfig.java`

**Copilot Prompt:**
```java
Create CacheConfig with @Configuration and @EnableCaching:
Define Caffeine cache manager with cache names:
- "dashboardStats" (TTL 5 min)
- "salesChart" (TTL 10 min)
- "topProducts" (TTL 5 min)

Use @Value to inject TTL from application.yml
```

---

### **File 12:** `service/StatisticsService.java`

**Copilot Prompt:**
```java
Create StatisticsService with @Service:

Method: StatisticCard buildStatCard(String title, Long value, BigDecimal salesValue, 
                                     Object compareValue, String compareText,
                                     String icon, String iconColor)
Calculate percentage change
Format currency/numbers
Return StatisticCard

Helper methods:
- String formatCurrency(BigDecimal amount)
- String formatNumber(Long number)
- Double calculatePercentageChange(Number current, Number previous)
```

---

### **File 13:** `service/AggregationService.java`

**Copilot Prompt:**
```java
Create AggregationService with @Service:

Method 1: DashboardStatsResponse aggregateStats()
- Call OrderServiceClient for orders data
- Call UserServiceClient for users data
- Use StatisticsService to build cards
- Return DashboardStatsResponse
- Add @Cacheable("dashboardStats")

Method 2: SalesChartResponse aggregateSalesChart(int year, int month)
- Call OrderServiceClient.getSalesByDate()
- Calculate total and average
- Return SalesChartResponse
- Add @Cacheable(value = "salesChart", key = "#year + '-' + #month")

Method 3: TopProductsResponse aggregateTopProducts(int limit)
- Call OrderServiceClient.getTopProducts()
- Call ProductServiceClient for each product to get details
- Merge data
- Return TopProductsResponse
- Add @Cacheable(value = "topProducts", key = "#limit")

Inject: OrderServiceClient, UserServiceClient, ProductServiceClient, StatisticsService
Add logging
```

**Expected Code:**
```java
package com.ecommerce.dashboardservice.service;

import com.ecommerce.dashboardservice.client.*;
import com.ecommerce.dashboardservice.dto.response.*;
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
        
        // 1. Pending Orders
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
        
        // 2. Total Sales
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
        
        // 3. Total Orders
        Long totalOrders = orderServiceClient.countTotalOrders();
        Long ordersLastWeek = orderServiceClient.countOrdersLastWeek();
        StatisticCard ordersCard = statisticsService.buildStatCard(
            "Total Order",
            totalOrders,
            null,
            ordersLastWeek,
            "from past week",
            "package",
            "#FEF3C7"
        );
        
        // 4. Total Users
        Long totalUsers = userServiceClient.countTotalUsers();
        Long usersYesterday = userServiceClient.countUsersYesterday();
        StatisticCard usersCard = statisticsService.buildStatCard(
            "Total User",
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
        
        List<SalesChartResponse.SalesDataPoint> data = 
            orderServiceClient.getSalesByDate(year, month);
        
        // Calculate totals
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
        
        List<TopProductsResponse.TopProduct> products = 
            orderServiceClient.getTopProducts(limit);
        
        // Enrich with product details from Product Service
        // (Optional - if Order Service doesn't have all details)
        products.forEach(product -> {
            try {
                // Get full product details
                // ProductDetails details = productServiceClient.getProductDetails(product.getProductId());
                // product.setImageUrl(details.getImageUrl());
                // product.setStock(details.getStock());
            } catch (Exception e) {
                log.warn("Failed to enrich product: {}", product.getProductId());
            }
        });
        
        return TopProductsResponse.builder()
            .products(products)
            .build();
    }
}
```

---

### **File 14:** `service/DashboardService.java`

**Copilot Prompt:**
```java
Create DashboardService with @Service:
This is a facade that delegates to AggregationService

Methods:
1. getDashboardStats() → calls aggregationService.aggregateStats()
2. getSalesChart(year, month) → calls aggregationService.aggregateSalesChart()
3. getTopProducts(limit) → calls aggregationService.aggregateTopProducts()

Add @CacheEvict annotations for manual cache clearing:
4. clearStatsCache() - evict "dashboardStats"
5. clearAllCache() - evict all caches

Inject: AggregationService
Add logging
```

---

## 🎮 PHASE 5: Controller (20 min)

### **File 15:** `controller/DashboardController.java`

**Copilot Prompt:**
```java
Create DashboardController with @RestController and @RequestMapping("/api/dashboard"):

Endpoints:
1. GET /stats
   - Call dashboardService.getDashboardStats()
   - Return DashboardStatsResponse

2. GET /sales-chart
   - @RequestParam year (default current year)
   - @RequestParam month (default current month)
   - Call dashboardService.getSalesChart()
   - Return SalesChartResponse

3. GET /top-products
   - @RequestParam limit (default 10)
   - Call dashboardService.getTopProducts()
   - Return TopProductsResponse

4. POST /cache/clear (admin only)
   - Call dashboardService.clearAllCache()
   - Return success message

Add @Slf4j for logging
Add proper HTTP status codes
```

**Expected Code:**
```java
package com.ecommerce.dashboardservice.controller;

import com.ecommerce.dashboardservice.dto.response.*;
import com.ecommerce.dashboardservice.service.DashboardService;
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
        DashboardStatsResponse stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/sales-chart")
    public ResponseEntity<SalesChartResponse> getSalesChart(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month
    ) {
        LocalDate now = LocalDate.now();
        if (year == null) year = now.getYear();
        if (month == null) month = now.getMonthValue();
        
        log.info("GET /api/dashboard/sales-chart?year={}&month={}", year, month);
        SalesChartResponse chart = dashboardService.getSalesChart(year, month);
        return ResponseEntity.ok(chart);
    }
    
    @GetMapping("/top-products")
    public ResponseEntity<TopProductsResponse> getTopProducts(
        @RequestParam(defaultValue = "10") Integer limit
    ) {
        log.info("GET /api/dashboard/top-products?limit={}", limit);
        TopProductsResponse products = dashboardService.getTopProducts(limit);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping("/cache/clear")
    public ResponseEntity<Map<String, String>> clearCache() {
        log.info("POST /api/dashboard/cache/clear");
        dashboardService.clearAllCache();
        return ResponseEntity.ok(Map.of(
            "message", "All caches cleared successfully"
        ));
    }
}
```

---

## 📡 ORDER SERVICE - NEW ENDPOINTS

**Dashboard Service cần Order Service expose các endpoints:**

### **File:** `order-service/controller/OrderStatsController.java`

**Copilot Prompt:**
```java
Create OrderStatsController in Order Service with @RestController and @RequestMapping("/api/orders/stats"):

Endpoints:
1. GET /pending-count - Count pending orders
2. GET /pending-count-yesterday - Count pending from yesterday
3. GET /total-sales - Sum total of confirmed+delivered orders
4. GET /total-sales-yesterday - Sum total from yesterday
5. GET /total-count - Count all confirmed+delivered orders
6. GET /count-last-week - Count orders from last 7 days
7. GET /sales-by-date?year=2024&month=10 - Group sales by date
8. GET /top-products?limit=10 - Top selling products

Each endpoint uses OrderRepository queries
Return simple data types (Long, BigDecimal, List)
Add @Slf4j logging
```

---

## ✅ DEPLOYMENT

### **Docker Compose:**

```yaml
# Add to docker-compose.yml

  dashboard-service:
    build: ./dashboard-service
    container_name: dashboard-service
    ports:
      - "8089:8089"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/dashboard_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
      SERVICES_ORDER_URL: http://order-service:8084
      SERVICES_USER_URL: http://user-service:8081
      SERVICES_PRODUCT_URL: http://product-service:8082
      SERVICES_PAYMENT_URL: http://payment-service:8085
    depends_on:
      - postgres
      - eureka-server
    networks:
      - ecommerce-network

  # Add database
  postgres:
    ports:
      - "5440:5432"
    environment:
      POSTGRES_DB: dashboard_db
```

---

## 🧪 TESTING

```bash
# 1. Start Dashboard Service
cd dashboard-service
mvn spring-boot:run

# 2. Test endpoints
curl http://localhost:8089/api/dashboard/stats

curl http://localhost:8089/api/dashboard/sales-chart?year=2024&month=10

curl http://localhost:8089/api/dashboard/top-products?limit=5

# 3. Clear cache
curl -X POST http://localhost:8089/api/dashboard/cache/clear
```

---

## 📋 CHECKLIST

```
Dashboard Service:
[ ] pom.xml created with dependencies
[ ] application.yml configured
[ ] WebClientConfig created (4 clients)
[ ] CacheConfig created
[ ] DTOs created (4 response classes)
[ ] OrderServiceClient created
[ ] UserServiceClient created
[ ] ProductServiceClient created
[ ] StatisticsService created
[ ] AggregationService created
[ ] DashboardService created
[ ] DashboardController created
[ ] Database dashboard_db created

Order Service Updates:
[ ] OrderStatsController created
[ ] 8 new stats endpoints added
[ ] Repository queries added

Testing:
[ ] All endpoints return data
[ ] Cache working (check logs)
[ ] WebClient calls successful
[ ] Frontend integration working
```

---

## 🎯 ARCHITECTURE BENEFITS

**Why separate Dashboard Service:**

1. ✅ **Separation of Concerns:** Dashboard logic riêng
2. ✅ **Scalability:** Scale dashboard độc lập
3. ✅ **Caching:** Cache ở service level
4. ✅ **Performance:** Không ảnh hưởng Order Service
5. ✅ **Aggregation:** Combine data từ nhiều services
6. ✅ **Security:** Có thể add admin auth riêng

---

**DASHBOARD SERVICE - COMPLETE MICROSERVICE! 📊**

Time: 3-4 hours | Files: 25+ | Port: 8089