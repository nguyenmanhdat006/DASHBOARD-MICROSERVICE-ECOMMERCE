# FRONTEND API CONTRACTS - DASHBOARD SERVICE

This document defines the REST API endpoints provided by the **Dashboard Service** (running on port `8089`) for frontend integration.

---

## 🎯 GENERAL INFORMATION

- **Base URL:** `http://localhost:8089`
- **Content-Type:** `application/json`
- **Response Format:** All successful responses return `200 OK`.

---

## 💻 ENDPOINTS SPECIFICATION

### **1. Get Dashboard Summary Statistics**
Fetch the summary cards containing total numbers, growth rates, formatting, and theme configurations for the dashboard widgets.

* **URL:** `/api/dashboard/stats`
* **Method:** `GET`
* **Auth Required:** No
* **Headers:** `Content-Type: application/json`

#### **Response Body Example (`200 OK`)**
```json
{
  "totalPending": {
    "title": "Total Pending",
    "value": 2040,
    "formattedValue": "2,040",
    "percentageChange": 1.8,
    "changeDirection": "up",
    "changeText": "from yesterday",
    "icon": "clock",
    "iconColor": "#FFE2E5"
  },
  "totalSales": {
    "title": "Total Sales",
    "value": 89000000,
    "formattedValue": "$89,000,000.00",
    "percentageChange": 4.5,
    "changeDirection": "up",
    "changeText": "from yesterday",
    "icon": "trending-up",
    "iconColor": "#DCFCE7"
  },
  "totalOrders": {
    "title": "Total Orders",
    "value": 10293,
    "formattedValue": "10,293",
    "percentageChange": -1.3,
    "changeDirection": "down",
    "changeText": "from past week",
    "icon": "package",
    "iconColor": "#FEF3C7"
  },
  "totalUsers": {
    "title": "Total Users",
    "value": 40689,
    "formattedValue": "40,689",
    "percentageChange": 8.4,
    "changeDirection": "up",
    "changeText": "from yesterday",
    "icon": "users",
    "iconColor": "#F3E8FF"
  }
}
```

---

### **2. Get Sales Chart Data**
Retrieve chronological sales data points for rendering line or bar charts representing revenue and order volume trends.

* **URL:** `/api/dashboard/sales-chart`
* **Method:** `GET`
* **Query Parameters:**
  - `year` *(optional, number)*: Defaults to the current year.
  - `month` *(optional, number)*: Defaults to the current month (1-12).

* **Example Request:** `GET /api/dashboard/sales-chart?year=2026&month=5`

#### **Response Body Example (`200 OK`)**
```json
{
  "data": [
    {
      "date": "2026-05-01",
      "label": "1",
      "sales": 45000000.00,
      "orderCount": 15
    },
    {
      "date": "2026-05-02",
      "label": "2",
      "sales": 52000000.00,
      "orderCount": 18
    },
    {
      "date": "2026-05-03",
      "label": "3",
      "sales": 48500000.00,
      "orderCount": 16
    }
  ],
  "period": "May 2026",
  "totalSales": 145500000.00,
  "averageSales": 48500000.00
}
```

---

### **3. Get Top Selling Products**
Fetch a list of top-performing items sold, sorted by quantity in descending order.

* **URL:** `/api/dashboard/top-products`
* **Method:** `GET`
* **Query Parameters:**
  - `limit` *(optional, number, default: 10)*: Maximum number of products to return.

* **Example Request:** `GET /api/dashboard/top-products?limit=5`

#### **Response Body Example (`200 OK`)**
```json
{
  "products": [
    {
      "productId": "prod-123",
      "productName": "iPhone 15 Pro Max",
      "size": "256GB",
      "imageUrl": "https://example.com/iphone.jpg",
      "price": 29990000.00,
      "stock": 150,
      "category": "Electronics",
      "totalSold": 245,
      "totalRevenue": 7347550000.00
    },
    {
      "productId": "prod-456",
      "productName": "AirPods Pro",
      "size": null,
      "imageUrl": "https://example.com/airpods.jpg",
      "price": 5990000.00,
      "stock": 320,
      "category": "Electronics",
      "totalSold": 189,
      "totalRevenue": 1132110000.00
    }
  ]
}
```

---

### **4. Clear Dashboard Cache**
Manually purge all cached statistics and chart data, forcing the Dashboard Service to reload fresh data from source microservices.

* **URL:** `/api/dashboard/cache/clear`
* **Method:** `POST`

#### **Response Body Example (`200 OK`)**
```json
{
  "message": "All caches cleared successfully"
}
```

---

## ⚡ TYPESCRIPT DEFINITIONS FOR FRONTEND

Copy and paste these interfaces into your React/Vue/Angular project (e.g., `src/types/dashboard.ts`):

```typescript
export interface StatisticCard {
  title: string;
  value: number | null;
  formattedValue: string;
  percentageChange: number;
  changeDirection: 'up' | 'down';
  changeText: string;
  icon: string;
  iconColor: string;
}

export interface DashboardStatsResponse {
  totalPending: StatisticCard;
  totalSales: StatisticCard;
  totalOrders: StatisticCard;
  totalUsers: StatisticCard;
}

export interface SalesDataPoint {
  date: string;
  label: string;
  sales: number;
  orderCount: number;
}

export interface SalesChartResponse {
  data: SalesDataPoint[];
  period: string;
  totalSales: number;
  averageSales: number;
}

export interface TopProduct {
  productId: string;
  productName: string;
  size: string | null;
  imageUrl: string;
  price: number;
  stock: number;
  category: string;
  totalSold: number;
  totalRevenue: number;
}

export interface TopProductsResponse {
  products: TopProduct[];
}

export interface ClearCacheResponse {
  message: string;
}
```
