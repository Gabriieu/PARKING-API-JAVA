INSERT INTO users (id, username, password, role, created_at, updated_at, created_by, updated_by)
VALUES (1, 'maria.silva@email.com', 'password', 'ROLE_ADMIN', NOW(), NOW(), null, null),
       (2, 'jose.souza@email.com', 'password', 'ROLE_CLIENT', NOW(), NOW(), null, null),
       (3, 'ana.pereira@email.com', 'password', 'ROLE_ADMIN', NOW(), NOW(), null, null),
       (4, 'carlos.oliveira@email.com', 'password', 'ROLE_CLIENT', NOW(), NOW(), null, null),
       (5, 'beatriz.santos@email.com', 'password', 'ROLE_CLIENT', NOW(), NOW(), null, null);
