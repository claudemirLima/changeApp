#!/bin/bash

# Script para executar migrations de um projeto específico
# Usage: ./scripts/migrate-project.sh [project] [environment] [action]

PROJECT=${1:-exchangeApi}
ENVIRONMENT=${2:-dev}
ACTION=${3:-migrate}

echo "🔄 Executando migrations do projeto: $PROJECT"
echo "📋 Ambiente: $ENVIRONMENT"
echo "📋 Ação: $ACTION"

# Verificar se o projeto existe
if [ ! -d "$PROJECT" ]; then
    echo "❌ Projeto não encontrado: $PROJECT"
    echo "Projetos disponíveis: exchangeApi, transactionApi, managerProductApi"
    exit 1
fi

# Configurações por ambiente
case $ENVIRONMENT in
    "dev")
        DB_URL="jdbc:postgresql://localhost:5433/changeapp_dev"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    "test")
        DB_URL="jdbc:postgresql://localhost:5434/changeapp_test"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    "hm")
        DB_URL="jdbc:postgresql://localhost:5432/changeapp_hm"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    "prd")
        DB_URL="jdbc:postgresql://localhost:5432/changeapp"
        DB_USER="changeapp"
        DB_PASSWORD="changeapp123"
        ;;
    *)
        echo "❌ Ambiente inválido: $ENVIRONMENT"
        echo "Ambientes válidos: dev, test, hm, prd"
        exit 1
        ;;
esac

# Executar ação do Flyway
case $ACTION in
    "migrate")
        echo "🚀 Executando migrations do $PROJECT..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" \
               -locations=filesystem:$PROJECT/src/main/resources/db/migration migrate
        ;;
    "info")
        echo "📊 Informações das migrations do $PROJECT..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" \
               -locations=filesystem:$PROJECT/src/main/resources/db/migration info
        ;;
    "validate")
        echo "✅ Validando migrations do $PROJECT..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" \
               -locations=filesystem:$PROJECT/src/main/resources/db/migration validate
        ;;
    "clean")
        echo "⚠️  Limpando banco de dados..."
        read -p "Tem certeza que deseja limpar o banco $ENVIRONMENT? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" \
                   -locations=filesystem:$PROJECT/src/main/resources/db/migration clean
        else
            echo "❌ Operação cancelada."
            exit 1
        fi
        ;;
    "baseline")
        echo "📋 Criando baseline para $PROJECT..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" \
               -locations=filesystem:$PROJECT/src/main/resources/db/migration baseline
        ;;
    *)
        echo "❌ Ação inválida: $ACTION"
        echo "Ações válidas: migrate, info, validate, clean, baseline"
        exit 1
        ;;
esac

echo "✅ Migrations do $PROJECT executadas com sucesso!" 