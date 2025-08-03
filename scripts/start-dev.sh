#!/bin/bash

# Script para iniciar o ambiente de desenvolvimento
echo "🚀 Iniciando ambiente de desenvolvimento ChangeApp..."

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

# Executar migrations de todos os projetos
echo "🔄 Executando migrations de todos os projetos..."
./scripts/migrate-all.sh dev migrate

# Verificar status dos serviços
echo "🔍 Verificando status dos serviços..."
docker-compose -f docker-compose.dev.yml ps

echo "✅ Ambiente de desenvolvimento iniciado com sucesso!"
echo ""
echo "📋 URLs das APIs:"
echo "  ExchangeApi: http://localhost:8081"
echo "  TransactionApi: http://localhost:8082"
echo "  ManagerProductApi: http://localhost:8083"
echo "  PostgreSQL: localhost:5433"
echo "  MongoDB: localhost:27018"
echo "  Redis: localhost:6379"
echo ""
echo "🔧 Configurações dos Bancos (Instâncias no mesmo PostgreSQL):"
echo "  ExchangeApi + TransactionApi:"
echo "    Database: changeapp_dev"
echo "    Username: changeapp"
echo "    Password: changeapp123"
echo "    Port: 5433"
echo ""
echo "  ManagerProductApi:"
echo "    Database: changeapp_product_dev"
echo "    Username: changeapp"
echo "    Password: changeapp123"
echo "    Port: 5433"
echo ""
echo "🔧 Configurações do MongoDB (TransactionApi):"
echo "  Database: changeapp_transactions_dev"
echo "  Username: changeapp"
echo "  Password: changeapp123"
echo "  Port: 27018"
echo ""
echo "💡 Todas as instâncias PostgreSQL estão no mesmo container!"
echo "💡 MongoDB está em container separado para o TransactionApi!" 