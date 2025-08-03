-- Migration V1: Create transaction schema
-- Description: Creates the initial database schema for TransactionApi
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Tabela de transações
CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id VARCHAR(36) NOT NULL UNIQUE,
    original_amount DECIMAL(10,4) NOT NULL,
    converted_amount DECIMAL(10,4) NOT NULL,
    rate DECIMAL(10,4) NOT NULL,
    from_currency_prefix VARCHAR(10) NOT NULL,
    to_currency_prefix VARCHAR(10) NOT NULL,
    product_id BIGINT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',
    reason TEXT,
    risk_score DECIMAL(3,2),
    warnings TEXT[],
    recommendations TEXT[],
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    expires_at TIMESTAMP NULL
);

-- Tabela de histórico de transações
CREATE TABLE IF NOT EXISTS transaction_history (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(36) NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20),
    details JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100)
);

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_transactions_transaction_id ON transactions(transaction_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions(status);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);
CREATE INDEX IF NOT EXISTS idx_transactions_expires_at ON transactions(expires_at);
CREATE INDEX IF NOT EXISTS idx_transaction_history_transaction_id ON transaction_history(transaction_id);
CREATE INDEX IF NOT EXISTS idx_transaction_history_created_at ON transaction_history(created_at); 