FROM ubuntu:latest AS build

# Atualizar os pacotes e instalar o OpenJDK 17
RUN apt-get update && apt-get install -y openjdk-17-jdk

# Instalar Gradle
RUN apt-get install -y wget unzip \
    && wget https://services.gradle.org/distributions/gradle-8.7-bin.zip -P /tmp \
    && unzip -d /opt/gradle /tmp/gradle-8.7-bin.zip \
    && ln -s /opt/gradle/gradle-8.7/bin/gradle /usr/bin/gradle

# Definir variáveis de ambiente
ENV GRADLE_HOME /opt/gradle/gradle-8.7
ENV PATH $PATH:/opt/gradle/gradle-8.7/bin

# Copiar o código fonte para o container
COPY . .

# Construir o projeto usando Gradle
RUN gradle clean build

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /build/libs/desafio-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]