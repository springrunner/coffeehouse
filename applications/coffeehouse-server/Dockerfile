# Run this command in the project root to build the Docker image
# docker build --progress=plain --no-cache -f applications/coffeehouse-server/Dockerfile --tag=coffeehouse-server:latest .

FROM bellsoft/liberica-runtime-container:jdk-21-crac-cds-slim-glibc as builder
WORKDIR coffeehouse-server
COPY gradle gradle
COPY --chmod=+x gradlew.bat . 
COPY --chmod=+x gradlew .
RUN ./gradlew --version --no-daemon
COPY . .
RUN ./gradlew clean applications:coffeehouse-server:build --no-daemon
RUN java -Djarmode=layertools -jar applications/coffeehouse-server/build/libs/coffeehouse-server-1.0.0.jar extract

FROM bellsoft/liberica-runtime-container:jre-21-crac-cds-slim-glibc
WORKDIR coffeehouse-server
COPY --from=builder coffeehouse-server/dependencies/ ./
COPY --from=builder coffeehouse-server/spring-boot-loader/ ./
COPY --from=builder coffeehouse-server/snapshot-dependencies/ ./
COPY --from=builder coffeehouse-server/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
