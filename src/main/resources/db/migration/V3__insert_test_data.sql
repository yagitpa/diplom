-- Вставка тестовых пользователей (USER и ADMIN)
-- Пароли закодированы с использованием BCrypt (пароли: "password", "admin")
INSERT INTO users (email, password, first_name, last_name, phone, role, image)
VALUES
    ('user@example.com',
     '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', -- password
     'Иван',
     'Иванов',
     '+7 (999) 123-45-67',
     'USER',
     NULL),
    ('admin@example.com',
     '$2a$10$Xl0yLzI1z5c5oZzF5z5c5uZzF5z5c5uZzF5z5c5uZzF5z5c5uZzF5z6', -- admin
     'Пётр',
     'Петров',
     '+7 (999) 765-43-21',
     'ADMIN',
     NULL);