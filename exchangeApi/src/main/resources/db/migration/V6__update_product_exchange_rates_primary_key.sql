-- Migration V6: Update product_exchange_rates primary key
-- Description: Remove effective_date from primary key of product_exchange_rates table
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Remover a chave primária atual
ALTER TABLE product_exchange_rates DROP CONSTRAINT IF EXISTS product_exchange_rates_pkey;

-- Adicionar nova chave primária sem effective_date
ALTER TABLE product_exchange_rates ADD CONSTRAINT product_exchange_rates_pkey 
    PRIMARY KEY (product_id, from_currency_prefix, to_currency_prefix, is_active);

-- Manter o índice para effective_date para performance
CREATE INDEX IF NOT EXISTS idx_product_exchange_rates_effective_date ON product_exchange_rates(effective_date); 