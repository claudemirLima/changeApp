-- Migration V2: Insert exchange initial data
-- Description: Inserts initial currencies and exchange rates for ExchangeApi
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Inserir moedas iniciais
INSERT INTO currencies (prefix, is_active, name, description) 
SELECT 'ORO', true, 'Ouro Real', 'Moeda oficial do Reino SRM'
WHERE NOT EXISTS (SELECT 1 FROM currencies WHERE prefix = 'ORO' AND is_active = true);

INSERT INTO currencies (prefix, is_active, name, description) 
SELECT 'TIB', true, 'Tibar', 'Moeda secundária do Reino SRM'
WHERE NOT EXISTS (SELECT 1 FROM currencies WHERE prefix = 'TIB' AND is_active = true);

-- Inserir taxas de câmbio iniciais
INSERT INTO exchange_rates (from_currency_prefix, to_currency_prefix, effective_date, is_active, rate) 
SELECT 'ORO', 'TIB', CURRENT_DATE, true, 2.5000
WHERE NOT EXISTS (SELECT 1 FROM exchange_rates WHERE from_currency_prefix = 'ORO' AND to_currency_prefix = 'TIB' AND effective_date = CURRENT_DATE AND is_active = true);

INSERT INTO exchange_rates (from_currency_prefix, to_currency_prefix, effective_date, is_active, rate) 
SELECT 'TIB', 'ORO', CURRENT_DATE, true, 0.4000
WHERE NOT EXISTS (SELECT 1 FROM exchange_rates WHERE from_currency_prefix = 'TIB' AND to_currency_prefix = 'ORO' AND effective_date = CURRENT_DATE AND is_active = true); 