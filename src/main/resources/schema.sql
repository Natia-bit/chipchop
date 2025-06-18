CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

insert INTO users (email, password)
values
    ('one@example.com', 'secretpass'),
    ('two.example.com', 'verysecretpass'),
    ('three@example.com', 'supersecretpass');


CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

insert INTO roles (name)
values
    ('user'),
    ('admin'),


CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (roles_id) REFERENCES role(id) ON DELETE CASCADE
)

