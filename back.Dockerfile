FROM openjdk:21

COPY build/libs/*.jar ./spring.jar

CMD ["java", "-jar", "./spring.jar"]