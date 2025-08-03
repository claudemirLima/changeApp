-- Migration V2: Insert exchange initial data
-- Description: Inserts initial currencies and exchange rates for ExchangeApi
-- Author: ChangeApp Team
-- Date: 2024-01-15

-- Inserir moedas iniciais
INSERT INTO currencies (prefix, is_active, name, description) VALUES
('ORO', true, 'Ouro Real', 'Moeda oficial do Reino SRM'),
('TIB', true, 'Tibar', 'Moeda secundária do Reino SRM')
ON CONFLICT (prefix, is_active) DO NOTHING;

-- Inserir taxas de câmbio iniciais
INSERT INTO exchange_rates (from_currency_prefix, to_currency_prefix, effective_date, is_active, rate) VALUES
('ORO', 'TIB', CURRENT_DATE, true, 2.5000),
('TIB', 'ORO', CURRENT_DATE, true, 0.4000)
ON CONFLICT (from_currency_prefix, to_currency_prefix, effective_date, is_active) DO NOTHING; 