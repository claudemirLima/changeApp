// =====================================================
// Script de Inicialização do MongoDB
// =====================================================

// Conectar ao banco admin
db = db.getSiblingDB('admin');

// Criar usuário para a aplicação
db.createUser({
    user: 'changeapp',
    pwd: 'changeapp123',
    roles: [
        {
            role: 'readWrite',
            db: 'changeapp_transactions'
        },
        {
            role: 'readWrite',
            db: 'changeapp_transactions_dev'
        },
        {
            role: 'readWrite',
            db: 'changeapp_transactions_test'
        },
        {
            role: 'readWrite',
            db: 'changeapp_transactions_hm'
        }
    ]
});

// Criar databases para diferentes ambientes
db = db.getSiblingDB('changeapp_transactions');
db.createCollection('transactions');

db = db.getSiblingDB('changeapp_transactions_dev');
db.createCollection('transactions');

db = db.getSiblingDB('changeapp_transactions_test');
db.createCollection('transactions');

db = db.getSiblingDB('changeapp_transactions_hm');
db.createCollection('transactions');

// Criar índices para otimização de consultas
db = db.getSiblingDB('changeapp_transactions');
db.transactions.createIndex({ "transactionId": 1 }, { unique: true });
db.transactions.createIndex({ "type": 1 });
db.transactions.createIndex({ "status": 1 });
db.transactions.createIndex({ "fromCurrencyPrefix": 1 });
db.transactions.createIndex({ "toCurrencyPrefix": 1 });
db.transactions.createIndex({ "kingdomId": 1 });
db.transactions.createIndex({ "fromProductId": 1 });
db.transactions.createIndex({ "toProductId": 1 });
db.transactions.createIndex({ "createdAt": 1 });
db.transactions.createIndex({ "originalAmount": 1 });

// Índices compostos
db.transactions.createIndex({ "type": 1, "status": 1 });
db.transactions.createIndex({ "fromCurrencyPrefix": 1, "toCurrencyPrefix": 1 });
db.transactions.createIndex({ "kingdomId": 1, "status": 1 });

// Repetir índices para outros ambientes
db = db.getSiblingDB('changeapp_transactions_dev');
db.transactions.createIndex({ "transactionId": 1 }, { unique: true });
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

db = db.getSiblingDB('changeapp_transactions_test');
db.transactions.createIndex({ "transactionId": 1 }, { unique: true });
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

db = db.getSiblingDB('changeapp_transactions_hm');
db.transactions.createIndex({ "transactionId": 1 }, { unique: true });
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

print("MongoDB inicializado com sucesso!");
print("Databases criados: changeapp_transactions, changeapp_transactions_dev, changeapp_transactions_test, changeapp_transactions_hm");
print("Usuário criado: changeapp");
print("Índices criados para otimização de consultas"); 