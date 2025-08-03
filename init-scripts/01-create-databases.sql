-- =====================================================
-- Script de Inicialização - Criação de Instâncias
-- =====================================================

-- Criar instância para ManagerProductApi
CREATE DATABASE changeapp_product;

-- Criar instâncias para ambientes de desenvolvimento
CREATE DATABASE changeapp_dev;
CREATE DATABASE changeapp_product_dev;

-- Criar instâncias para ambientes de teste
CREATE DATABASE changeapp_test;
CREATE DATABASE changeapp_product_test;

-- Criar instâncias para ambientes de homologação
CREATE DATABASE changeapp_hm;
CREATE DATABASE changeapp_product_hm;

-- Comentários das instâncias
COMMENT ON DATABASE changeapp IS 'Instância para ExchangeApi e TransactionApi (Produção)';
COMMENT ON DATABASE changeapp_product IS 'Instância para ManagerProductApi (Produção)';
COMMENT ON DATABASE changeapp_dev IS 'Instância para ExchangeApi e TransactionApi (Desenvolvimento)';
COMMENT ON DATABASE changeapp_product_dev IS 'Instância para ManagerProductApi (Desenvolvimento)';
COMMENT ON DATABASE changeapp_test IS 'Instância para ExchangeApi e TransactionApi (Teste)';
COMMENT ON DATABASE changeapp_product_test IS 'Instância para ManagerProductApi (Teste)';
COMMENT ON DATABASE changeapp_hm IS 'Instância para ExchangeApi e TransactionApi (Homologação)';
COMMENT ON DATABASE changeapp_product_hm IS 'Instância para ManagerProductApi (Homologação)'; 