# Sistema de Conversão de Moedas - Reino SRM

## Visão Geral

Sistema de conversão de moedas para o reino SRM, implementado com três APIs modulares seguindo princípios de microserviços:

- **ExchangeApi**: Gestão de taxas de câmbio e conversões
- **TransactionApi**: Registro e consulta de transações
- **ManagerProductApi**: Gestão de produtos, reinos e valorização

## Arquitetura de Microserviços

### 🏗️ **Princípios Aplicados:**
- **Banco de Dados por Serviço**: Cada API possui seu próprio banco de dados
- **Independência**: Serviços podem ser desenvolvidos, testados e implantados independentemente
- **Isolamento**: Falhas em um serviço não afetam outros
- **Escalabilidade**: Cada serviço pode ser escalado independentemente

### 🗄️ **Bancos de Dados:**

#### **ExchangeApi + TransactionApi (PostgreSQL)**
- **Banco**: `changeapp` (PostgreSQL)
- **Porta**: 5432 (Produção) / 5433 (Desenvolvimento)
- **Usuário**: `changeapp`
- **Tabelas**: `currencies`, `exchange_rates`, `product_exchange_rates`, `transactions`

#### **ManagerProductApi (PostgreSQL)**
- **Banco**: `changeapp_product` (PostgreSQL)
- **Porta**: 5432 (Produção) / 5433 (Desenvolvimento)
- **Usuário**: `changeapp`
- **Tabelas**: `kingdoms`, `products`

#### **TransactionApi (MongoDB)**
- **Banco**: `changeapp_transactions` (MongoDB)
- **Porta**: 27017 (Produção) / 27018 (Desenvolvimento)
- **Usuário**: `changeapp`
- **Collections**: `transactions`

### 🐳 **Vantagens da Arquitetura:**
- **Flexibilidade**: Cada serviço usa a tecnologia mais adequada
- **Performance**: MongoDB para consultas complexas de transações
- **Escalabilidade**: Cada banco pode ser otimizado independentemente
- **Manutenção**: Isolamento de falhas e atualizações

## Estrutura do Projeto

```
changeApp/
├── exchangeApi/           # API de Conversão de Moedas
│   ├── src/main/java/com/exchange/
│   │   ├── controller/    # Controllers REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Acesso a dados
│   │   └── domain/        # Entidades e DTOs
│   └── pom.xml
├── transactionApi/        # API de Transações
│   ├── src/main/java/com/transaction/
│   │   ├── controller/    # Controllers REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Acesso a dados
│   │   └── domain/        # Entidades e DTOs
│   └── pom.xml
├── managerProductApi/     # API de Gestão de Produtos
│   ├── src/main/java/com/product/
│   │   ├── controller/    # Controllers REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Acesso a dados
│   │   └── domain/        # Entidades e DTOs
│   └── pom.xml
└── pom.xml               # POM principal
```

## Tecnologias

- **Java 21**
- **Spring Boot 3.3.2**
- **Maven** (Multi-módulo)
- **PostgreSQL** (ExchangeApi + ManagerProductApi)
- **MongoDB** (TransactionApi)
- **Redis** (Cache compartilhado)
- **Flyway** (Migrations PostgreSQL)
- **Lombok** (Redução de boilerplate)

## APIs

### ExchangeApi
Responsável por:
- Gestão de taxas de câmbio entre moedas
- Conversões em tempo real
- Configuração de taxas por produto
- **Banco**: `changeapp` (PostgreSQL)

### TransactionApi
Responsável por:
- Registro de transações comerciais
- Histórico de transações
- Relatórios e consultas avançadas
- **Banco**: `changeapp_transactions` (MongoDB)

### ManagerProductApi
Responsável por:
- Gestão de produtos (peles, madeira, hidromel)
- Gestão de reinos
- Sistema de valorização/desvalorização
- Controle de inflação por reino
- **Banco**: `changeapp_product` (PostgreSQL)

## Scripts SQL

### Banco ExchangeApi + TransactionApi (`changeapp`)

```sql
-- Tabelas para ExchangeApi
CREATE TABLE currencies (
    prefix VARCHAR(10) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    deactivated_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    PRIMARY KEY (prefix, is_active)
);

CREATE TABLE exchange_rates (
    from_currency_prefix VARCHAR(10) NOT NULL,
    to_currency_prefix VARCHAR(10) NOT NULL,
    effective_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    rate DECIMAL(10,4) NOT NULL,
    deactivated_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    PRIMARY KEY (from_currency_prefix, to_currency_prefix, effective_date, is_active)
);

CREATE TABLE product_exchange_rates (
    product_id BIGINT NOT NULL,
    from_currency_prefix VARCHAR(10) NOT NULL,
    to_currency_prefix VARCHAR(10) NOT NULL,
    effective_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    base_rate DECIMAL(10,4) NOT NULL,
    product_multiplier DECIMAL(5,2) NOT NULL,
    deactivated_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    PRIMARY KEY (product_id, from_currency_prefix, to_currency_prefix, effective_date, is_active)
);
```

### Banco ManagerProductApi (`changeapp_product`)

```sql
-- Tabela de Reinos
CREATE TABLE kingdoms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    quality_rate DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    is_owner BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deactivated_at TIMESTAMP
);

-- Tabela de Produtos
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    base_value DECIMAL(10,2) NOT NULL,
    demand_quantifier DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    quality_qualifier DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    kingdom_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deactivated_at TIMESTAMP,
    
    CONSTRAINT fk_products_kingdom FOREIGN KEY (kingdom_id) REFERENCES kingdoms(id),
    CONSTRAINT chk_products_base_value CHECK (base_value > 0),
    CONSTRAINT chk_products_demand_quantifier CHECK (demand_quantifier > 0),
    CONSTRAINT chk_products_quality_qualifier CHECK (quality_qualifier > 0)
);
```

### Banco TransactionApi (`changeapp_transactions`)

```javascript
// Collection: transactions
{
  "_id": "uuid-do-exchange-api",
  "transactionId": "uuid-do-exchange-api",
  "type": "CONVERSION|EXCHANGE",
  "status": "REQUESTED|APPROVED|NOT_APPROVED|WARNING",
  "originalAmount": 100.00,
  "convertedAmount": 250.00,
  "fromCurrencyPrefix": "OR",
  "toCurrencyPrefix": "TB",
  "exchangeRate": 2.50,
  "fromProductId": 1,
  "fromProductName": "Hidromel",
  "toProductId": 2,
  "toProductName": "Armas",
  "kingdomId": 1,
  "kingdomName": "SRM",
  "reason": "Troca comercial",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "completedAt": "2024-01-01T10:05:00"
}

// Índices criados automaticamente:
// - transactionId (único)
// - type
// - status
// - fromCurrencyPrefix
// - toCurrencyPrefix
// - kingdomId
// - fromProductId
// - toProductId
// - createdAt
// - originalAmount
// - Compostos: (type, status), (fromCurrencyPrefix, toCurrencyPrefix), (kingdomId, status)
```

## Scripts de Gerenciamento

### MongoDB
```bash
# Conectar ao MongoDB
./scripts/mongodb-manager.sh dev connect

# Backup do MongoDB
./scripts/mongodb-manager.sh dev backup

# Restaurar backup
./scripts/mongodb-manager.sh dev restore backup_file.gz

# Ver logs
./scripts/mongodb-manager.sh dev logs

# Ver estatísticas
./scripts/mongodb-manager.sh dev stats
```

### PostgreSQL
```bash
# Migrar ExchangeApi + ManagerProductApi
./scripts/migrate-all.sh dev migrate

# Migrar ManagerProductApi
./scripts/migrate-product.sh dev migrate
``` 