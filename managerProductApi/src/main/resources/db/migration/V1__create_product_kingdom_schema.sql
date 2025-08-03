-- =====================================================
-- Schema para ManagerProductApi - Banco Separado
-- =====================================================

-- Tabela de Reinos
CREATE TABLE IF NOT EXISTS kingdoms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    quality_rate DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    is_owner BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deactivated_at TIMESTAMP
);

-- Índices para Reinos
CREATE INDEX IF NOT EXISTS idx_kingdoms_name ON kingdoms(name);
CREATE INDEX IF NOT EXISTS idx_kingdoms_is_active ON kingdoms(is_active);
CREATE INDEX IF NOT EXISTS idx_kingdoms_is_owner ON kingdoms(is_owner);
CREATE INDEX IF NOT EXISTS idx_kingdoms_quality_rate ON kingdoms(quality_rate);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    base_value DECIMAL(10,2) NOT NULL,
    demand_quantifier DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    quality_qualifier DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    kingdom_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deactivated_at TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_products_kingdom FOREIGN KEY (kingdom_id) REFERENCES kingdoms(id),
    CONSTRAINT chk_products_base_value CHECK (base_value > 0),
    CONSTRAINT chk_products_demand_quantifier CHECK (demand_quantifier > 0),
    CONSTRAINT chk_products_quality_qualifier CHECK (quality_qualifier > 0)
);

-- Índices para Produtos
CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_kingdom_id ON products(kingdom_id);
CREATE INDEX IF NOT EXISTS idx_products_is_active ON products(is_active);
CREATE INDEX IF NOT EXISTS idx_products_base_value ON products(base_value);
CREATE INDEX IF NOT EXISTS idx_products_demand_quantifier ON products(demand_quantifier);
CREATE INDEX IF NOT EXISTS idx_products_quality_qualifier ON products(quality_qualifier);

-- Índice composto para busca por categoria e ativo
CREATE INDEX IF NOT EXISTS idx_products_category_active ON products(category, is_active);

-- Índice composto para busca por reino e ativo
CREATE INDEX IF NOT EXISTS idx_products_kingdom_active ON products(kingdom_id, is_active);

-- Comentários das tabelas
COMMENT ON TABLE kingdoms IS 'Tabela que armazena os reinos do sistema';
COMMENT ON TABLE products IS 'Tabela que armazena os produtos do sistema';

-- Comentários das colunas
COMMENT ON COLUMN kingdoms.quality_rate IS 'Taxa de qualidade do reino (0.1 a 10.0)';
COMMENT ON COLUMN kingdoms.is_owner IS 'Indica se é o reino proprietário do sistema';
COMMENT ON COLUMN products.base_value IS 'Valor base do produto';
COMMENT ON COLUMN products.demand_quantifier IS 'Quantificador de demanda do produto (0.1 a 10.0)';
COMMENT ON COLUMN products.quality_qualifier IS 'Qualificador de qualidade do produto (0.1 a 10.0)'; 