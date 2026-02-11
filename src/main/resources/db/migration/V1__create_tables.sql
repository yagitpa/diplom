-- Создание таблицы 'users'
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name VARCHAR(50)  NOT NULL,
    phone VARCHAR(20)  NOT NULL,
    role VARCHAR(10)  NOT NULL,
    image VARCHAR(512)
);

-- Создание таблицы 'ads'
CREATE TABLE IF NOT EXISTS ads (
    pk SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price INTEGER NOT NULL,
    image VARCHAR(512),
    user_id INTEGER NOT NULL
);

-- Создание таблицы 'comments'
CREATE TABLE IF NOT EXISTS comments (
    pk SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id INTEGER NOT NULL,
    ad_id INTEGER NOT NULL
);