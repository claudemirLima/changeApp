#!/bin/bash

# Script para criar nova migration
# Usage: ./scripts/create-migration.sh "description"

if [ $# -eq 0 ]; then
    echo "❌ Descrição da migration é obrigatória"
    echo "Usage: ./scripts/create-migration.sh \"description\""
    exit 1
fi

DESCRIPTION=$1

# Obter a próxima versão
LAST_MIGRATION=$(ls migrations/V*.sql 2>/dev/null | sort -V | tail -n 1)

if [ -z "$LAST_MIGRATION" ]; then
    NEXT_VERSION="V1"
else
    # Extrair número da última migration
    LAST_VERSION=$(basename "$LAST_MIGRATION" .sql | sed 's/V\([0-9]*\).*/\1/')
    NEXT_VERSION="V$((LAST_VERSION + 1))"
fi

# Criar nome do arquivo
FILENAME="migrations/${NEXT_VERSION}__${DESCRIPTION// /_}.sql"

echo "📝 Criando nova migration: $FILENAME"

# Criar arquivo de migration
cat > "$FILENAME" << EOF
-- Migration ${NEXT_VERSION}: ${DESCRIPTION}
-- Description: ${DESCRIPTION}
-- Author: ChangeApp Team
-- Date: $(date +%Y-%m-%d)

-- TODO: Adicionar SQL da migration aqui

EOF

echo "✅ Migration criada: $FILENAME"
echo "📝 Edite o arquivo e adicione o SQL necessário" 