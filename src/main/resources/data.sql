-- Script SQL para popular o banco de dados em desenvolvimento (H2)
-- Este arquivo é executado automaticamente pelo Spring Boot no perfil 'dev'

-- Inserir categorias de RECEITA
INSERT INTO categorias (id, nome, descricao, tipo, cor) VALUES 
(1, 'Salário', 'Salário mensal', 'RECEITA', '#4CAF50'),
(2, 'Freelance', 'Trabalhos freelance e projetos extras', 'RECEITA', '#8BC34A'),
(3, 'Investimentos', 'Rendimentos de investimentos', 'RECEITA', '#009688'),
(4, 'Outros', 'Outras receitas', 'RECEITA', '#00BCD4');

-- Inserir categorias de DESPESA
INSERT INTO categorias (id, nome, descricao, tipo, cor) VALUES 
(5, 'Alimentação', 'Supermercado, restaurantes e delivery', 'DESPESA', '#F44336'),
(6, 'Transporte', 'Combustível, transporte público e Uber', 'DESPESA', '#FF9800'),
(7, 'Moradia', 'Aluguel, condomínio e contas da casa', 'DESPESA', '#FF5722'),
(8, 'Saúde', 'Plano de saúde, medicamentos e consultas', 'DESPESA', '#E91E63'),
(9, 'Lazer', 'Entretenimento, viagens e hobbies', 'DESPESA', '#9C27B0'),
(10, 'Educação', 'Cursos, livros e materiais de estudo', 'DESPESA', '#3F51B5');

-- Inserir transações de exemplo (mês atual)
INSERT INTO transacoes (id, descricao, valor, tipo, data, categoria_id, observacoes, criado_em, atualizado_em) VALUES 
(1, 'Salário Outubro', 5000.00, 'RECEITA', CURRENT_DATE - 25, 1, 'Salário mensal depositado', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Freelance - Website', 1500.00, 'RECEITA', CURRENT_DATE - 20, 2, 'Desenvolvimento de website para cliente', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Supermercado', 450.00, 'DESPESA', CURRENT_DATE - 18, 5, 'Compras do mês', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Aluguel', 1200.00, 'DESPESA', CURRENT_DATE - 5, 7, 'Aluguel mensal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Combustível', 300.00, 'DESPESA', CURRENT_DATE - 10, 6, 'Abastecimento do carro', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Plano de Saúde', 450.00, 'DESPESA', CURRENT_DATE - 7, 8, 'Mensalidade plano de saúde', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Restaurante', 180.00, 'DESPESA', CURRENT_DATE - 3, 5, 'Almoço em família', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Cinema', 120.00, 'DESPESA', CURRENT_DATE - 2, 9, 'Ingressos de cinema', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Curso Online', 200.00, 'DESPESA', CURRENT_DATE - 15, 10, 'Curso de Spring Boot', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Dividendos', 250.00, 'RECEITA', CURRENT_DATE - 1, 3, 'Dividendos de ações', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
