# # Stage 1: Build với Maven
# FROM maven:3.9.6-eclipse-temurin-17 AS builder
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests

# FROM openjdk:17-jdk-slim
# VOLUME /tmp
# ARG JAVA_OPTS
# ENV JAVA_OPTS=$JAVA_OPTS
# COPY target/ConfigServer-0.0.1-SNAPSHOT.jar configserver.jar
# EXPOSE 8888
# ENTRYPOINT exec java $JAVA_OPTS -jar configserver.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar configserver.jar


# =========================
# Stage 1: Build JAR
# =========================
# FROM maven:3.9.6-eclipse-temurin-17 AS builder
# WORKDIR /app

# # Copy pom.xml và preload dependencies để tận dụng cache
# COPY pom.xml .
# RUN mvn dependency:go-offline -B

# # Copy source code và build
# COPY src ./src
# RUN mvn clean package -DskipTests

# # =========================
# # Stage 2: Runtime
# # =========================
# FROM eclipse-temurin:17-jre-jammy AS runtime
# WORKDIR /app

# # Copy file jar từ stage builder
# COPY --from=builder /app/target/*.jar app.jar

# # Tạo user non-root (bảo mật tốt hơn)
# RUN useradd -ms /bin/bash configserver
# USER configserver

# # Cổng mặc định, có thể override bằng docker-compose
# EXPOSE 8888

# # Healthcheck (tùy chỉnh theo service)
# # - Mặc định check actuator/health
# # - Có thể override ở docker-compose nếu cổng khác
# # HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=5 \
# CMD curl -f http://localhost:8888/actuator/health || exit 1

# # Chạy app
# ENTRYPOINT ["java","-jar","app.jar"]


# =========================
# Stage 1: Build JAR
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom.xml và preload dependencies (tận dụng cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build JAR
COPY src ./src
RUN mvn clean package -DskipTests

# =========================
# Stage 2: Runtime (siêu nhẹ với Alpine)
# =========================
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Copy file JAR từ builder
COPY --from=builder /app/target/*.jar app.jar

# Tạo user non-root để tăng bảo mật
RUN addgroup -S configserver && adduser -S configserver -G configserver
USER configserver:configserver

# Cổng mặc định (override bằng docker-compose nếu khác)
EXPOSE 8888

# Chạy ứng dụng
ENTRYPOINT ["java","-jar","app.jar"]
