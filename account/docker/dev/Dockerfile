FROM openjdk:11-oracle
COPY target/account.jar account.jar
ENTRYPOINT ["java","-jar","/account.jar", "--spring.profiles.active=dev"]
