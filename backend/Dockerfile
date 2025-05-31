FROM openjdk:20
ARG JAR_FILE=build/libs/ElectronicShop-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]