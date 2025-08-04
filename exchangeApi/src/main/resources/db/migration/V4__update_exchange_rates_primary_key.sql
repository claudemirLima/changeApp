-- Migration V4: Update exchange_rates primary key
-- Description: Remove effective_date from primary key of exchange_rates table
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Remover a chave primária atual
ALTER TABLE exchange_rates DROP CONSTRAINT IF EXISTS exchange_rates_pkey;

-- Adicionar nova chave primária sem effective_date
ALTER TABLE exchange_rates ADD CONSTRAINT exchange_rates_pkey 
    PRIMARY KEY (from_currency_prefix, to_currency_prefix, is_active);

-- Manter o índice para effective_date para performance
CREATE INDEX IF NOT EXISTS idx_exchange_rates_effective_date ON exchange_rates(effective_date); 