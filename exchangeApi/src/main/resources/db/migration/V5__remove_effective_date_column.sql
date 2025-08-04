-- Migration V5: Remove effective_date column
-- Description: Remove effective_date column from exchange_rates table completely
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Remover a coluna effective_date da tabela exchange_rates
ALTER TABLE exchange_rates DROP COLUMN IF EXISTS effective_date;

-- Remover índices relacionados ao effective_date
DROP INDEX IF EXISTS idx_exchange_rates_effective_date;
DROP INDEX IF EXISTS idx_exchange_rates_date; 