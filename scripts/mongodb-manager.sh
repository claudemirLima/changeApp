#!/bin/bash

# Script para gerenciar o MongoDB
# Uso: ./scripts/mongodb-manager.sh [dev|test|hm|prd] [connect|backup|restore|logs]

ENVIRONMENT=${1:-dev}
ACTION=${2:-connect}

echo "🗄️ Gerenciando MongoDB..."
echo "📋 Ambiente: $ENVIRONMENT"
echo "🎯 Ação: $ACTION"

# Configurações por ambiente
case $ENVIRONMENT in
    "dev")
        CONTAINER_NAME="changeapp-mongodb-dev"
        PORT="27018"
        DATABASE="changeapp_transactions_dev"
        ;;
    "test")
        CONTAINER_NAME="changeapp-mongodb-test"
        PORT="27019"
        DATABASE="changeapp_transactions_test"
        ;;
    "hm")
        CONTAINER_NAME="changeapp-mongodb-hm"
        PORT="27020"
        DATABASE="changeapp_transactions_hm"
        ;;
    "prd")
        CONTAINER_NAME="changeapp-mongodb"
        PORT="27017"
        DATABASE="changeapp_transactions"
        ;;
    *)
        echo "❌ Ambiente inválido. Use: dev, test, hm ou prd"
        exit 1
        ;;
esac

# Verificar se o container está rodando
if ! docker ps | grep -q $CONTAINER_NAME; then
    echo "❌ Container $CONTAINER_NAME não está rodando"
    echo "💡 Execute: docker-compose -f docker-compose.dev.yml up -d"
    exit 1
fi

case $ACTION in
    "connect")
        echo "🔌 Conectando ao MongoDB..."
        echo "📊 Container: $CONTAINER_NAME"
        echo "🌐 Porta: $PORT"
        echo "🗃️ Database: $DATABASE"
        echo ""
        echo "🔧 Comandos úteis:"
        echo "  - show dbs                    # Listar databases"
        echo "  - use $DATABASE              # Usar database"
        echo "  - show collections           # Listar collections"
        echo "  - db.transactions.find()     # Listar transações"
        echo "  - db.transactions.count()    # Contar transações"
        echo "  - exit                       # Sair"
        echo ""
        docker exec -it $CONTAINER_NAME mongosh -u changeapp -p changeapp123 --authenticationDatabase admin
        ;;
    "backup")
        echo "💾 Fazendo backup do MongoDB..."
        BACKUP_DIR="./backups/mongodb"
        mkdir -p $BACKUP_DIR
        
        BACKUP_FILE="$BACKUP_DIR/${DATABASE}_$(date +%Y%m%d_%H%M%S).gz"
        docker exec $CONTAINER_NAME mongodump --db $DATABASE --archive --gzip > $BACKUP_FILE
        
        echo "✅ Backup criado: $BACKUP_FILE"
        ;;
    "restore")
        echo "📥 Restaurando backup do MongoDB..."
        BACKUP_DIR="./backups/mongodb"
        
        if [ -z "$3" ]; then
            echo "❌ Especifique o arquivo de backup"
            echo "💡 Uso: $0 $ENVIRONMENT restore <arquivo_backup>"
            exit 1
        fi
        
        BACKUP_FILE="$BACKUP_DIR/$3"
        if [ ! -f "$BACKUP_FILE" ]; then
            echo "❌ Arquivo de backup não encontrado: $BACKUP_FILE"
            exit 1
        fi
        
        docker exec -i $CONTAINER_NAME mongorestore --db $DATABASE --archive --gzip < $BACKUP_FILE
        echo "✅ Backup restaurado com sucesso"
        ;;
    "logs")
        echo "📋 Exibindo logs do MongoDB..."
        docker logs $CONTAINER_NAME
        ;;
    "stats")
        echo "📊 Estatísticas do MongoDB..."
        docker exec $CONTAINER_NAME mongosh -u changeapp -p changeapp123 --authenticationDatabase admin --eval "
            use $DATABASE;
            print('=== ESTATÍSTICAS DO MONGODB ===');
            print('Database: ' + db.getName());
            print('Collections: ' + db.getCollectionNames().join(', '));
            print('Total de transações: ' + db.transactions.count());
            print('Transações por status:');
            db.transactions.aggregate([
                { \$group: { _id: '\$status', count: { \$sum: 1 } } },
                { \$sort: { count: -1 } }
            ]).forEach(function(doc) {
                print('  ' + doc._id + ': ' + doc.count);
            });
            print('Transações por tipo:');
            db.transactions.aggregate([
                { \$group: { _id: '\$type', count: { \$sum: 1 } } },
                { \$sort: { count: -1 } }
            ]).forEach(function(doc) {
                print('  ' + doc._id + ': ' + doc.count);
            });
        "
        ;;
    *)
        echo "❌ Ação inválida. Use: connect, backup, restore, logs ou stats"
        exit 1
        ;;
esac

echo "✅ Operação concluída!" 