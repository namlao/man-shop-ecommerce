# 🛒 Microservices E-Commerce Platform

## 📌 Giới thiệu
Dự án cá nhân xây dựng hệ thống **E-Commerce Backend** theo kiến trúc **Microservices**.  
Mục tiêu: học tập và áp dụng **Spring Boot, Microservices, DevOps, Observability** vào một hệ thống thực tế.  

⚠️ Lưu ý: Đây là **learning project**, code chưa tối ưu hoàn toàn cho production, nhưng đã hoàn thiện nhiều milestone quan trọng (Authentication, Cart, Order, Monitoring, Docker).  

---

## 🚀 Công nghệ sử dụng
- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA  
- **Microservices**: API Gateway, Eureka Server, Config Server  
- **Authentication**: JWT (Login/Register/Refresh/Logout, Role-based Authorization)  
- **Database**: MySQL, H2 (cho test)  
- **Resilience & Observability**: Resilience4j, Spring Boot Actuator, Prometheus, Grafana  
- **DevOps**: Dockerfile, Docker Compose (multi-service network), CI/CD
- **Testing**: JUnit5, Mockito (Unit Test), MockMvc (Integration Test – in progress)  

---

## ✅ Milestone (Cập nhật)
### Milestone 1: Core Microservices & Communication (DONE)
- Product Service (CRUD)  
- Order Service (CRUD, call Product Service, tính giá)  
- User Service (CRUD, hash password)  
- Auth Service (JWT Login/Register/Refresh/Logout)  
- API Gateway (routing + JWT filter)  

### Milestone 2: Security & Authorization (DONE)
- Role-based authorization (`@PreAuthorize`)  
- Access Token + Refresh Token flow  

### Milestone 3: Business Features (DONE)
- Cart Service (CRUD, checkout)  
- Checkout flow → Order (trừ stock Product)  
- Gắn order với userId từ JWT  

### Milestone 4: Resilience & Observability (DONE)
- Resilience4j (retry, fallback, circuit breaker)  
- Spring Cloud Config Server  
- Eureka Server (service registry)  
- Prometheus + Grafana Dashboard  

### Milestone 5: DevOps & Deployment (DONE)
- Dockerfile cho từng service  
- Docker Compose (multi-service network, profiles dev/prod)  
- MySQL Centralized Database  
- CI/CD pipeline (GitHub Actions/Jenkins – TODO)  

### Milestone 6+: Testing & Advanced (IN PROGRESS)
- Integration & End-to-End Tests  (Cart flow IT: DONE,Auth flow IT: DONE,Order flow IT: IN PROGRESS)
- Payment Service (mock)  
- Kafka Integration (event-driven flow)  
- Kubernetes Deployment  

---

## 🖥️ Cách chạy thử (local)
### Yêu cầu
- Docker & Docker Compose  
- JDK 17  
- Maven 3.9+  

### Các bước
```bash
# Clone repo
git clone https://github.com/namlao/man-shop-ecommerce.git
cd man-shop-ecommerce

# Build toàn bộ services
mvn clean install 

# Chạy bằng Docker Compose
docker compose up -d

# Các service sẽ chạy tại:
# - API Gateway: http://localhost:8080
# - Eureka Dashboard: http://localhost:8761
# - Prometheus: http://localhost:9090
# - Grafana: http://localhost:3000
