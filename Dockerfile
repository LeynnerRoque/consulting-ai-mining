# Estágio 1: Build clonando direto do GitHub para garantir que os arquivos existam
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /project

# Instala o git caso a imagem não tenha
RUN apt-get update && apt-get install -y git

# Clona o seu repositório diretamente para dentro da pasta de trabalho
RUN git clone https://github.com/LeynnerRoque/consulting-ai-mining.git .

# Executa o empacotamento do Quarkus
RUN mvn clean package -DskipTests

# Estágio 2: Runtime (Usando JRE 21)
FROM eclipse-temurin:21-jre
ENV LANGUAGE='en_US:en'
WORKDIR /deployments

COPY --from=build /project/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /project/target/quarkus-app/*.jar /deployments/
COPY --from=build /project/target/quarkus-app/app/ /deployments/app/
COPY --from=build /project/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185

ENTRYPOINT ["java", "-Dquarkus.http.host=0.0.0.0", "-Djava.util.logging.manager=org.jboss.logmanager.LogManager", "-jar", "/deployments/quarkus-run.jar"]