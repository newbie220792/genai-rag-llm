FROM eclipse-temurin:17-jre-jammy
COPY chat-service-1.0.jar chat-service-1.0.jar

ENV spring.profiles.active prod

ENTRYPOINT ["java","-jar","chat-service-1.0.jar"]