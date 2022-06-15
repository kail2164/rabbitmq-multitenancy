FROM openjdk:11-oracle
COPY target/api-gateway.jar api-gateway.jar
ENTRYPOINT ["java","-jar","/api-gateway.jar", "--spring.profiles.active=dev"]
