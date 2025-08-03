-- Migration V1: Create exchange schema
-- Description: Creates the initial database schema for ExchangeApi
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Tabela de moedas
CREATE TABLE IF NOT EXISTS currencies (
    prefix VARCHAR(10) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    deactivated_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    PRIMARY KEY (prefix, is_active)
);

-- Tabela de taxas de câmbio
CREATE TABLE IF NOT EXISTS exchange_rates (
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

-- Tabela de taxas de câmbio por produto
CREATE TABLE IF NOT EXISTS product_exchange_rates (
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

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_currencies_prefix ON currencies(prefix);
CREATE INDEX IF NOT EXISTS idx_currencies_active ON currencies(is_active);
CREATE INDEX IF NOT EXISTS idx_exchange_rates_from_to ON exchange_rates(from_currency_prefix, to_currency_prefix);
CREATE INDEX IF NOT EXISTS idx_exchange_rates_date ON exchange_rates(effective_date);
CREATE INDEX IF NOT EXISTS idx_exchange_rates_active ON exchange_rates(is_active);
CREATE INDEX IF NOT EXISTS idx_product_exchange_rates_product ON product_exchange_rates(product_id);
CREATE INDEX IF NOT EXISTS idx_product_exchange_rates_from_to ON product_exchange_rates(from_currency_prefix, to_currency_prefix);
CREATE INDEX IF NOT EXISTS idx_product_exchange_rates_date ON product_exchange_rates(effective_date);
CREATE INDEX IF NOT EXISTS idx_product_exchange_rates_active ON product_exchange_rates(is_active); 