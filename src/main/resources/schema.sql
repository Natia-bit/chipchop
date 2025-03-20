CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

insert INTO "user"(email, password)
values
    ('one@example.com', 'secretpass'),
    ('two.example.com', 'verysecretpass')
    ('three@example.com', 'supersecretpass')