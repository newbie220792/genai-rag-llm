FROM eclipse-temurin:17-jre-jammy
COPY embedding-service-1.0.jar embedding-service-1.0.jar

ENV spring.profiles.active prod

ENTRYPOINT ["java","-jar","embedding-service-1.0.jar"]