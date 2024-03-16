# Run this command in the project root to build the Docker image
# docker build --progress=plain --no-cache -f applications/coffeehouse-edge-server/Dockerfile --tag=coffeehouse-edge-server:latest .

FROM bellsoft/liberica-runtime-container:jdk-21-crac-cds-slim-glibc as builder
WORKDIR coffeehouse-edge-server
COPY gradle gradle
COPY --chmod=+x gradlew.bat . 
COPY --chmod=+x gradlew .
RUN ./gradlew --version --no-daemon
COPY . .
RUN ./gradlew clean applications:coffeehouse-edge-server:build --no-daemon
RUN java -Djarmode=layertools -jar applications/coffeehouse-edge-server/build/libs/coffeehouse-edge-server-1.0.0.jar extract

FROM bellsoft/liberica-runtime-container:jre-21-crac-cds-slim-glibc
WORKDIR coffeehouse-edge-server
COPY --from=builder coffeehouse-edge-server/dependencies/ ./
COPY --from=builder coffeehouse-edge-server/spring-boot-loader/ ./
COPY --from=builder coffeehouse-edge-server/snapshot-dependencies/ ./
COPY --from=builder coffeehouse-edge-server/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
