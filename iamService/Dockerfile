

# ---- build stage ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- runtime stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod \
    TZ=America/Bogota \
    JAVA_OPTS="-Xms256m -Xmx512m"

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar solo pom.xml para cachear dependencias
COPY pom.xml .
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:resolve

# Copia del código fuente (para build rápido dentro del contenedor)
COPY src ./src

# Default: compilar y ejecutar
CMD ["mvn", "spring-boot:run"]