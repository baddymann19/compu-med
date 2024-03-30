FROM eclipse-temurin:17-jdk AS mavenbuilder

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw .
COPY src src
COPY pom.xml .
# separate step for downloading dependencies, only executed when pom or settings have changed
RUN ./mvnw dependency:go-offline

RUN ./mvnw clean package -DskipTests -Dquarkus.package.type=uber-jar

# app container for the native executable
FROM eclipse-temurin:17-jdk
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0"

WORKDIR /deployments/

COPY --from=mavenbuilder /app/target/*-runner.jar /deployments/app.jar
EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-Djava.security.egd=file:/dev/./urandom", "-jar", "/deployments/app.jar" ]