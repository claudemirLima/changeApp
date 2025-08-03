#!/bin/bash

# Script para executar migrations de todos os projetos
# Usage: ./scripts/migrate-all.sh [environment] [action]

ENVIRONMENT=${1:-dev}
ACTION=${2:-migrate}

echo "🔄 Executando migrations de todos os projetos"
echo "📋 Ambiente: $ENVIRONMENT"
echo "📋 Ação: $ACTION"

# Lista de projetos
PROJECTS=("exchangeApi" "transactionApi" "managerProductApi")

# Executar migrations para cada projeto
for PROJECT in "${PROJECTS[@]}"; do
    echo ""
    echo "📦 Executando migrations do $PROJECT..."
    ./scripts/migrate-project.sh "$PROJECT" "$ENVIRONMENT" "$ACTION"
    
    if [ $? -ne 0 ]; then
        echo "❌ Erro ao executar migrations do $PROJECT"
        exit 1
    fi
done

echo ""
echo "✅ Migrations de todos os projetos executadas com sucesso!" 