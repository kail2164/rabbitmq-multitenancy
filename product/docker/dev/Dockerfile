FROM openjdk:11-oracle
COPY target/product.jar product.jar
ENTRYPOINT ["java","-jar","/product.jar", "--spring.profiles.active=dev"]
