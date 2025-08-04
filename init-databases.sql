-- Script de inicialização para criar múltiplos bancos de dados
-- Este script será executado quando o container PostgreSQL for iniciado
-- Se os bancos já existirem, eles serão ignorados

-- Criar banco de dados para ExchangeApi (se não existir)
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_dev') THEN
        CREATE DATABASE changeapp_dev;
        RAISE NOTICE 'Banco changeapp_dev criado com sucesso';
    ELSE
        RAISE NOTICE 'Banco changeapp_dev já existe, pulando...';
    END IF;
END
$$;

-- Criar banco de dados para ManagerProductApi (se não existir)
SELECT 'CREATE DATABASE changeapp_product_dev'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_product_dev')\gexec

-- Criar banco de dados para TransactionApi (se não existir)
SELECT 'CREATE DATABASE changeapp_transactions_dev'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_transactions_dev')\gexec

-- Conceder privilégios ao usuário changeapp em todos os bancos (se existirem)
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_dev') THEN
        GRANT ALL PRIVILEGES ON DATABASE changeapp_dev TO changeapp;
        RAISE NOTICE 'Privilégios concedidos para changeapp_dev';
    END IF;
    
    IF EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_product_dev') THEN
        GRANT ALL PRIVILEGES ON DATABASE changeapp_product_dev TO changeapp;
        RAISE NOTICE 'Privilégios concedidos para changeapp_product_dev';
    END IF;
    
    IF EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_transactions_dev') THEN
        GRANT ALL PRIVILEGES ON DATABASE changeapp_transactions_dev TO changeapp;
        RAISE NOTICE 'Privilégios concedidos para changeapp_transactions_dev';
    END IF;
END
$$;

-- Conectar ao banco changeapp_dev e conceder privilégios no schema public (se existir)
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_dev') THEN
        EXECUTE 'GRANT ALL ON SCHEMA public TO changeapp';
        EXECUTE 'GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO changeapp';
        EXECUTE 'GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO changeapp';
        RAISE NOTICE 'Privilégios de schema concedidos para changeapp_dev';
    END IF;
END
$$;

-- Conectar ao banco changeapp_product_dev e conceder privilégios no schema public (se existir)
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_product_dev') THEN
        EXECUTE 'GRANT ALL ON SCHEMA public TO changeapp';
        EXECUTE 'GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO changeapp';
        EXECUTE 'GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO changeapp';
        RAISE NOTICE 'Privilégios de schema concedidos para changeapp_product_dev';
    END IF;
END
$$;

-- Conectar ao banco changeapp_transactions_dev e conceder privilégios no schema public (se existir)
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_database WHERE datname = 'changeapp_transactions_dev') THEN
        EXECUTE 'GRANT ALL ON SCHEMA public TO changeapp';
        EXECUTE 'GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO changeapp';
        EXECUTE 'GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO changeapp';
        RAISE NOTICE 'Privilégios de schema concedidos para changeapp_transactions_dev';
    END IF;
END
$$;

-- Mostrar status final dos bancos
SELECT 
    datname as database_name,
    CASE 
        WHEN datname IN ('changeapp_dev', 'changeapp_product_dev', 'changeapp_transactions_dev') 
        THEN '✅ Configurado'
        ELSE '❌ Não configurado'
    END as status
FROM pg_database 
WHERE datname IN ('changeapp_dev', 'changeapp_product_dev', 'changeapp_transactions_dev')
ORDER BY datname; 