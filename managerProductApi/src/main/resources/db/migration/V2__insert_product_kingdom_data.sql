-- =====================================================
-- Dados Iniciais para ManagerProductApi
-- =====================================================

-- Inserção de Reinos
INSERT INTO kingdoms (name, description, quality_rate, is_owner, is_active) VALUES
('SRM', 'Reino Antigo de SRM - Terra de magia e comércio', 1.50, TRUE, TRUE),
('Montanhas dos Anões', 'Reino dos anões nas montanhas distantes', 2.00, FALSE, TRUE),
('Floresta dos Elfos', 'Reino élfico nas florestas sagradas', 1.80, FALSE, TRUE),
('Deserto dos Nômades', 'Reino nômade nas areias do deserto', 0.80, FALSE, TRUE),
('Ilhas dos Mercadores', 'Reino mercantil nas ilhas do sul', 1.20, FALSE, TRUE);

-- Inserção de Produtos
INSERT INTO products (name, description, category, base_value, demand_quantifier, quality_qualifier, kingdom_id, is_active) VALUES
-- Produtos do Reino SRM (Reino Proprietário)
('Pergaminhos Mágicos', 'Pergaminhos com encantamentos antigos', 'Artefatos', 250.00, 2.50, 1.80, 1, TRUE),
('Poções de Cura', 'Poções mágicas de cura', 'Poções', 150.00, 1.80, 1.20, 1, TRUE),

-- Produtos do Reino dos Anões
('Armas de Aço', 'Armas forjadas pelos mestres anões', 'Armas', 500.00, 3.00, 2.50, 2, TRUE),
('Joias Raras', 'Joias preciosas das minas anãs', 'Joias', 800.00, 2.80, 2.20, 2, TRUE),
('Hidromel Real', 'Hidromel da melhor qualidade', 'Bebidas', 120.00, 1.50, 1.80, 2, TRUE),

-- Produtos do Reino dos Elfos
('Arcos Élficos', 'Arcos mágicos da floresta sagrada', 'Armas', 400.00, 2.20, 1.90, 3, TRUE),
('Ervas Medicinais', 'Ervas raras da floresta élfica', 'Medicina', 200.00, 1.90, 1.70, 3, TRUE),
('Tecidos de Seda', 'Tecidos finos dos elfos', 'Tecidos', 180.00, 1.60, 1.85, 3, TRUE),

-- Produtos do Reino dos Nômades
('Especiarias Raras', 'Especiarias exóticas do deserto', 'Especiarias', 300.00, 2.10, 0.90, 4, TRUE),
('Tapetes Artesanais', 'Tapetes tecidos pelos nômades', 'Decoração', 220.00, 1.40, 0.85, 4, TRUE),
('Camelos', 'Camelos resistentes do deserto', 'Animais', 600.00, 1.70, 0.95, 4, TRUE),

-- Produtos do Reino dos Mercadores
('Pérolas do Mar', 'Pérolas raras das ilhas', 'Joias', 450.00, 2.30, 1.30, 5, TRUE),
('Peixes Exóticos', 'Peixes raros das águas profundas', 'Alimentos', 280.00, 1.80, 1.25, 5, TRUE),
('Navegação', 'Serviços de navegação marítima', 'Serviços', 350.00, 2.00, 1.15, 5, TRUE);

-- Comentários sobre os dados inseridos
COMMENT ON TABLE kingdoms IS 'Dados iniciais dos reinos do sistema';
COMMENT ON TABLE products IS 'Dados iniciais dos produtos do sistema'; 