#!/bin/bash

# Script para parar o ambiente
echo "🛑 Parando ambiente ChangeApp..."

# Parar containers
echo "🐳 Parando containers..."
docker-compose down

echo "✅ Ambiente parado com sucesso!" 