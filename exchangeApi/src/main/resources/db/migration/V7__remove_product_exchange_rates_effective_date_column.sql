-- Migration V7: Remove effective_date column from product_exchange_rates
-- Description: Remove effective_date column from product_exchange_rates table completely
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Remover a coluna effective_date da tabela product_exchange_rates
ALTER TABLE product_exchange_rates DROP COLUMN IF EXISTS effective_date;

-- Remover índices relacionados ao effective_date
DROP INDEX IF EXISTS idx_product_exchange_rates_effective_date;
DROP INDEX IF EXISTS idx_product_exchange_rates_date; 