INSERT INTO audit.organizacoes (id, nome, ativo, created_at)
VALUES (1, 'Guilda Teste', true, CURRENT_TIMESTAMP);

INSERT INTO audit.usuarios (id, organizacao_id, nome, email, senha_hash, status, created_at, updated_at)
VALUES (1, 1, 'Usuário Teste', 'teste@email.com', '123', 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);