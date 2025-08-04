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





