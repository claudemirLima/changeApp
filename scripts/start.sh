#!/bin/bash

# Script para iniciar o ambiente
echo "🚀 Iniciando ambiente ChangeApp..."

# Verificar se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Build das aplicações
echo "🏗️  Fazendo build das aplicações..."
./scripts/build.sh

# Iniciar ambiente de produção
echo "🐳 Iniciando containers..."
docker-compose up -d

# Aguardar serviços ficarem prontos
echo "⏳ Aguardando serviços ficarem prontos..."
sleep 30

# Verificar status dos serviços
echo "🔍 Verificando status dos serviços..."
docker-compose ps

echo "✅ Ambiente iniciado com sucesso!"
echo ""
echo "📋 URLs das APIs:"
echo "  ExchangeApi: http://localhost:8081"
echo "  TransactionApi: http://localhost:8082"
echo "  ManagerProductApi: http://localhost:8083"
echo "  PostgreSQL: localhost:5432"
echo "  Redis: localhost:6379"
echo "  H2 Console (dev): http://localhost:9092" 