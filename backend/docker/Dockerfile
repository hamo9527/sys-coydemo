FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY cailu-bom-common ./cailu-bom-common
COPY cailu-bom-system ./cailu-bom-system
COPY cailu-bom-erp ./cailu-bom-erp
COPY cailu-bom-oa ./cailu-bom-oa
COPY cailu-bom-finance ./cailu-bom-finance
COPY cailu-bom-bootstrap ./cailu-bom-bootstrap
RUN mvn -q -DskipTests package -pl cailu-bom-bootstrap -am

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/cailu-bom-bootstrap/target/cailu-bom-bootstrap-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
