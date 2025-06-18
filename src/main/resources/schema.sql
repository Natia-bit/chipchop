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
    name TEXT NOT NULL
);

insert INTO roles (name)
values
    ('user'),
    ('admin'),

