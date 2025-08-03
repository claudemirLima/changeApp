#!/bin/bash

# Script para executar migrations do banco de dados
# Usage: ./scripts/migrate.sh [environment] [action]

ENVIRONMENT=${1:-dev}
ACTION=${2:-migrate}

echo "🔄 Executando migrations para ambiente: $ENVIRONMENT"
echo "📋 Ação: $ACTION"

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

# Verificar se o Flyway está instalado
if ! command -v flyway &> /dev/null; then
    echo "❌ Flyway não está instalado. Instalando..."
    
    # Baixar Flyway
    FLYWAY_VERSION="9.22.3"
    FLYWAY_URL="https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/${FLYWAY_VERSION}/flyway-commandline-${FLYWAY_VERSION}-linux-x64.tar.gz"
    
    echo "📥 Baixando Flyway $FLYWAY_VERSION..."
    wget -q $FLYWAY_URL -O flyway.tar.gz
    
    echo "📦 Extraindo Flyway..."
    tar -xzf flyway.tar.gz
    
    echo "🔧 Configurando Flyway..."
    sudo mv flyway-${FLYWAY_VERSION} /opt/flyway
    sudo ln -sf /opt/flyway/flyway /usr/local/bin/flyway
    
    echo "🧹 Limpando arquivos temporários..."
    rm flyway.tar.gz
    
    echo "✅ Flyway instalado com sucesso!"
fi

# Executar ação do Flyway
case $ACTION in
    "migrate")
        echo "🚀 Executando migrations..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" -locations=filesystem:migrations migrate
        ;;
    "info")
        echo "📊 Informações das migrations..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" -locations=filesystem:migrations info
        ;;
    "validate")
        echo "✅ Validando migrations..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" -locations=filesystem:migrations validate
        ;;
    "clean")
        echo "⚠️  Limpando banco de dados..."
        read -p "Tem certeza que deseja limpar o banco $ENVIRONMENT? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" -locations=filesystem:migrations clean
        else
            echo "❌ Operação cancelada."
            exit 1
        fi
        ;;
    "baseline")
        echo "📋 Criando baseline..."
        flyway -url="$DB_URL" -user="$DB_USER" -password="$DB_PASSWORD" -locations=filesystem:migrations baseline
        ;;
    *)
        echo "❌ Ação inválida: $ACTION"
        echo "Ações válidas: migrate, info, validate, clean, baseline"
        exit 1
        ;;
esac

echo "✅ Migrations executadas com sucesso!" 