#!/bin/bash

# Script para build das aplicações
echo "🏗️  Iniciando build das aplicações..."

# Build do projeto pai
echo "📦 Build do projeto pai..."
mvn clean install -DskipTests

# Build das APIs
echo "🔨 Build do ExchangeApi..."
cd exchangeApi
mvn clean package -DskipTests
cd ..

echo "🔨 Build do TransactionApi..."
cd transactionApi
mvn clean package -DskipTests
cd ..

echo "🔨 Build do ManagerProductApi..."
cd managerProductApi
mvn clean package -DskipTests
cd ..

echo "✅ Build concluído com sucesso!" 