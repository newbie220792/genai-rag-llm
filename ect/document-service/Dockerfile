FROM eclipse-temurin:17-jre-jammy
COPY document-service-1.0.jar document-service-1.0.jar

ENV spring.profiles.active prod

ENTRYPOINT ["java","-jar","document-service-1.0.jar"]