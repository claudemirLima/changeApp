# Dockerfile para ExchangeApi
FROM openjdk:21-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR da aplicação
COPY target/exchangeApi-*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8081

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prd
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 