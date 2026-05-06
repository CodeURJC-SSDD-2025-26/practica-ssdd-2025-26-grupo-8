# Stage 1: Build — uses Maven image so no JDK needed on the host machine
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Download dependencies first (separate layer for cache efficiency)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src/ src/

# Generate a self-signed SSL certificate and bundle it into the JAR via classpath
RUN rm -f src/main/resources/keystore.p12 && keytool -genkeypair \
    -alias virtus \
    -keyalg RSA \
    -keysize 2048 \
    -validity 3650 \
    -dname "CN=localhost, OU=Virtus, O=VirtusFitness, L=Madrid, ST=Madrid, C=ES" \
    -keystore src/main/resources/keystore.p12 \
    -storetype PKCS12 \
    -storepass virtusfitness \
    -noprompt

RUN mvn package -DskipTests -B

# Stage 2: Runtime — minimal JRE image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]
