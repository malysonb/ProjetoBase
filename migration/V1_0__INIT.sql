-- DROP SCHEMA auth;

CREATE SCHEMA auth AUTHORIZATION postgres;

INSERT INTO auth.role (nivel) VALUES ('ROLE_USER'), ('ROLE_ADMIN') ON CONFLICT DO NOTHING;

INSERT INTO auth.usuario (id, is_ativo, data_atualizacao, data_criacao, "version", ativado, email, login, nome, senha, role_name) VALUES(1, true, now(), now(), 0, true, 'mude.me@alterar.com', 'cumaru', 'Cumaru', '$2a$10$4z90FoQhXWW1PNRk3Q/h5ep/2L1jXNC0QPf8.KIHu9tkmR/el4kw6', 'ROLE_ADMIN');