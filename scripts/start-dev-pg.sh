#!/bin/bash

# Script para iniciar o ambiente de desenvolvimento com PostgreSQL
echo "🚀 Iniciando ambiente de desenvolvimento ChangeApp com PostgreSQL..."

# Verificar se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Iniciar ambiente de desenvolvimento
echo "🐳 Iniciando containers de desenvolvimento..."
docker-compose -f docker-compose.dev.yml up -d

# Aguardar serviços ficarem prontos
echo "⏳ Aguardando serviços ficarem prontos..."
sleep 45

# Verificar status dos serviços
echo "🔍 Verificando status dos serviços..."
docker-compose -f docker-compose.dev.yml ps

echo "✅ Ambiente de desenvolvimento com PostgreSQL iniciado com sucesso!"
echo ""
echo "📋 URLs das APIs:"
echo "  ExchangeApi: http://localhost:8081"
echo "  TransactionApi: http://localhost:8082"
echo "  ManagerProductApi: http://localhost:8083"
echo "  PostgreSQL: localhost:5433"
echo "  Redis: localhost:6379"
echo ""
echo "🔧 Configurações do Banco:"
echo "  Database: changeapp_dev"
echo "  Username: changeapp"
echo "  Password: changeapp123"
echo "  Port: 5433" 