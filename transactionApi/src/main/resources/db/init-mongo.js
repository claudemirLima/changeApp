// =====================================================
// Script de Inicialização do MongoDB
// =====================================================

// Conectar ao banco admin
db = db.getSiblingDB('admin');

// Verificar se o usuário já existe antes de criar
var existingUser = db.getUser('changeapp');
if (!existingUser) {
    // Criar usuário para a aplicação
    db.createUser({
        user: 'changeapp',
        pwd: 'changeapp123',
        roles: [
            
            {
                role: 'readWrite',
                db: 'changeapp_transactions_dev'
            }
        ]
    });
    print("Usuário changeapp criado com sucesso!");
} else {
    print("Usuário changeapp já existe, pulando criação...");
}

db = db.getSiblingDB('changeapp_transactions_dev');
db.createCollection('transactions');



// Repetir índices para outros ambientes
db = db.getSiblingDB('changeapp_transactions_dev');
// Removido índice único em transactionId pois agora é o _id
db.transactions.createIndex({ "type": 1 });
db.transactions.createIndex({ "status": 1 });
db.transactions.createIndex({ "fromCurrencyPrefix": 1 });
db.transactions.createIndex({ "toCurrencyPrefix": 1 });
db.transactions.createIndex({ "kingdomId": 1 });
db.transactions.createIndex({ "fromProductId": 1 });
db.transactions.createIndex({ "toProductId": 1 });
db.transactions.createIndex({ "createdAt": 1 });
db.transactions.createIndex({ "originalAmount": 1 });
db.transactions.createIndex({ "type": 1, "status": 1 });
db.transactions.createIndex({ "fromCurrencyPrefix": 1, "toCurrencyPrefix": 1 });
db.transactions.createIndex({ "kingdomId": 1, "status": 1 });

