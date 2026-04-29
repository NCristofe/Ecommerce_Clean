-- =========================
-- USUARIOS
-- =========================
INSERT INTO usuario (id, nome, email, telefone, senha, roles)
VALUES (UUID_TO_BIN('a0000000-0000-4000-8000-000000000001'), 'User1', 'user1@email.com', '11900000001', '123456',
        'USER'),
       (UUID_TO_BIN('a0000000-0000-4000-8000-000000000002'), 'User2', 'user2@email.com', '11900000002', '123456',
        'USER'),
       (UUID_TO_BIN('a0000000-0000-4000-8000-000000000003'), 'User3', 'user3@email.com', '11900000003', '123456',
        'USER'),
       (UUID_TO_BIN('a0000000-0000-4000-8000-000000000004'), 'User4', 'user4@email.com', '11900000004', '123456',
        'USER'),
       (UUID_TO_BIN('a0000000-0000-4000-8000-000000000005'), 'User5', 'user5@email.com', '11900000005', '123456',
        'USER');

-- =========================
-- CATEGORIAS
-- =========================
INSERT INTO categoria (id, nome)
VALUES (UUID_TO_BIN('b0000000-0000-4000-8000-000000000001'), 'Categoria1'),
       (UUID_TO_BIN('b0000000-0000-4000-8000-000000000002'), 'Categoria2'),
       (UUID_TO_BIN('b0000000-0000-4000-8000-000000000003'), 'Categoria3');

-- =========================
-- PRODUTOS
-- =========================
INSERT INTO produto (id, descricao, preco, img_url)
VALUES (UUID_TO_BIN('c0000000-0000-4000-8000-000000000001'), 'Produto1', 10.0, 'img1.jpg'),
       (UUID_TO_BIN('c0000000-0000-4000-8000-000000000002'), 'Produto2', 20.0, 'img2.jpg'),
       (UUID_TO_BIN('c0000000-0000-4000-8000-000000000003'), 'Produto3', 30.0, 'img3.jpg');

-- =========================
-- RELAÇÃO PRODUTO_CATEGORIA
-- =========================
INSERT INTO tb_produto_categoria (produto_id, categoria_id)
VALUES (UUID_TO_BIN('c0000000-0000-4000-8000-000000000001'), UUID_TO_BIN('b0000000-0000-4000-8000-000000000001')),
       (UUID_TO_BIN('c0000000-0000-4000-8000-000000000002'), UUID_TO_BIN('b0000000-0000-4000-8000-000000000002'));

-- =========================
-- PEDIDOS
-- =========================
INSERT INTO pedido (id, id_user, momento, status, client_id)
VALUES (UUID_TO_BIN('d0000000-0000-4000-8000-000000000001'),
        UUID_TO_BIN('a0000000-0000-4000-8000-000000000001'),
        '2026-04-01', 'PAGO',
        UUID_TO_BIN('a0000000-0000-4000-8000-000000000001')),

       (UUID_TO_BIN('d0000000-0000-4000-8000-000000000002'),
        UUID_TO_BIN('a0000000-0000-4000-8000-000000000002'),
        '2026-04-02', 'PAGO',
        UUID_TO_BIN('a0000000-0000-4000-8000-000000000002'));

-- =========================
-- ITENS DO PEDIDO
-- =========================
INSERT INTO tb_pedido_item (pedido_id, produto_id, quantidade, preco)
VALUES (UUID_TO_BIN('d0000000-0000-4000-8000-000000000001'),
        UUID_TO_BIN('c0000000-0000-4000-8000-000000000001'),
        2, 10.0),

       (UUID_TO_BIN('d0000000-0000-4000-8000-000000000002'),
        UUID_TO_BIN('c0000000-0000-4000-8000-000000000002'),
        1, 20.0);

-- =========================
-- PAGAMENTOS (MapsId)
-- =========================
INSERT INTO pagamento (pedido_id, momento)
VALUES (UUID_TO_BIN('d0000000-0000-4000-8000-000000000001'), '2026-04-02'),
       (UUID_TO_BIN('d0000000-0000-4000-8000-000000000002'), '2026-04-03');