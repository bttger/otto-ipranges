FROM maven:3-openjdk-11-slim AS BUILDER
WORKDIR /app
COPY pom.xml .
RUN mvn --batch-mode dependency:go-offline
COPY . .
RUN mvn --batch-mode install spring-boot:repackage -Dmaven.test.skip=true

FROM openjdk:11-jre
RUN groupadd -r ipranges && useradd -r -g ipranges ipranges
USER ipranges
WORKDIR /app
COPY --from=BUILDER /app/target/ipranges-0.0.1.jar .
CMD ["java", "-jar", "./ipranges-0.0.1.jar"]
EXPOSE 8080
