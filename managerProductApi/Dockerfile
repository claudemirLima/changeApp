# Dockerfile para ManagerProductApi
FROM openjdk:21-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR da aplicação
COPY target/managerProductApi-*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8083

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=dev
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]