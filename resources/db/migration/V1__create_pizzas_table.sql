-- Таблица пицц
CREATE TABLE pizzas (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Добавим тестовые данные
INSERT INTO pizzas (name, description, price) VALUES
    ('Маргарита', 'Томатный соус, моцарелла, базилик', 450.00),
    ('Пепперони', 'Томатный соус, моцарелла, пепперони', 550.00),
    ('Четыре сыра', 'Моцарелла, пармезан, горгонзола, фета', 650.00),
    ('Мексиканская', 'Томатный соус, моцарелла, говядина, перец халапеньо', 600.00);