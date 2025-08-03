#!/bin/bash

# Script para executar migrações do ManagerProductApi
# Uso: ./scripts/migrate-product.sh [dev|test|hm|prd] [migrate|clean|info]

ENVIRONMENT=${1:-dev}
ACTION=${2:-migrate}

echo "🔄 Executando migrações do ManagerProductApi..."
echo "📋 Ambiente: $ENVIRONMENT"
echo "🎯 Ação: $ACTION"

# Configurações por ambiente
case $ENVIRONMENT in
    "dev")
        DB_HOST="localhost"
        DB_PORT="5433"
        DB_NAME="changeapp_product_dev"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    "test")
        DB_HOST="localhost"
        DB_PORT="5433"
        DB_NAME="changeapp_product_test"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    "hm")
        DB_HOST="localhost"
        DB_PORT="5433"
        DB_NAME="changeapp_product_hm"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    "prd")
        DB_HOST="localhost"
        DB_PORT="5432"
        DB_NAME="changeapp_product"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    *)
        echo "❌ Ambiente inválido. Use: dev, test, hm ou prd"
        exit 1
        ;;
esac

# Verificar se o banco está acessível
echo "🔍 Verificando conectividade com o banco..."
if ! pg_isready -h $DB_HOST -p $DB_PORT -U $DB_USER > /dev/null 2>&1; then
    echo "❌ Banco não está acessível em $DB_HOST:$DB_PORT"
    echo "💡 Certifique-se de que o container está rodando"
    exit 1
fi

# Executar ação do Flyway
echo "🚀 Executando Flyway para ManagerProductApi..."
cd managerProductApi

case $ACTION in
    "migrate")
        echo "📦 Executando migrações..."
        mvn flyway:migrate \
            -Dflyway.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \
            -Dflyway.user=$DB_USER \
            -Dflyway.password=$DB_PASSWORD
        ;;
    "clean")
        echo "🧹 Limpando banco..."
        mvn flyway:clean \
            -Dflyway.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \
            -Dflyway.user=$DB_USER \
            -Dflyway.password=$DB_PASSWORD
        ;;
    "info")
        echo "ℹ️ Informações das migrações..."
        mvn flyway:info \
            -Dflyway.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \
            -Dflyway.user=$DB_USER \
            -Dflyway.password=$DB_PASSWORD
        ;;
    *)
        echo "❌ Ação inválida. Use: migrate, clean ou info"
        exit 1
        ;;
esac

cd ..

echo "✅ Migrações do ManagerProductApi concluídas!" 