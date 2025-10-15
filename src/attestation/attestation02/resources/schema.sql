-- ============================================
-- АТТЕСТАЦИЯ 02: СОЗДАНИЕ БАЗЫ ДАННЫХ
-- Автор: [Игорь]
-- Дата: 2025-01-15
-- ============================================

-- Удаляем таблицы, если они существуют (для повторного запуска)
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS products CASCADE;

-- ============================================
-- ТАБЛИЦА: Товары (Products)
-- ============================================
CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0)
);

COMMENT ON TABLE products IS 'Таблица товаров: содержит информацию о продуктах, их стоимости и количестве на складе';
COMMENT ON COLUMN products.id IS 'Уникальный идентификатор товара';
COMMENT ON COLUMN products.description IS 'Описание товара';
COMMENT ON COLUMN products.price IS 'Стоимость товара в рублях';
COMMENT ON COLUMN products.quantity IS 'Количество товара на складе';

-- ============================================
-- ТАБЛИЦА: Покупатели (Customers)
-- ============================================
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

COMMENT ON TABLE customers IS 'Таблица покупателей: содержит информацию о клиентах магазина';
COMMENT ON COLUMN customers.id IS 'Уникальный идентификатор покупателя';
COMMENT ON COLUMN customers.first_name IS 'Имя покупателя';
COMMENT ON COLUMN customers.last_name IS 'Фамилия покупателя';

-- ============================================
-- ТАБЛИЦА: Заказы (Orders)
-- ============================================
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    order_date DATE NOT NULL DEFAULT CURRENT_DATE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

COMMENT ON TABLE orders IS 'Таблица заказов: связывает покупателей с товарами и хранит информацию о заказах';
COMMENT ON COLUMN orders.id IS 'Уникальный идентификатор заказа';
COMMENT ON COLUMN orders.product_id IS 'ID товара из таблицы products';
COMMENT ON COLUMN orders.customer_id IS 'ID покупателя из таблицы customers';
COMMENT ON COLUMN orders.order_date IS 'Дата оформления заказа';
COMMENT ON COLUMN orders.quantity IS 'Количество товаров в заказе';

-- ============================================
-- ЗАПОЛНЕНИЕ ТАБЛИЦЫ: Товары
-- ============================================
INSERT INTO products (description, price, quantity) VALUES
('Ноутбук ASUS VivoBook 15', 45990.00, 15),
('Смартфон Samsung Galaxy A54', 32990.00, 30),
('Наушники Sony WH-1000XM5', 28990.00, 25),
('Клавиатура механическая HyperX Alloy', 8990.00, 40),
('Мышь Logitech MX Master 3', 7490.00, 50),
('Монитор Dell 27" 4K', 35990.00, 12),
('Веб-камера Logitech C920', 6990.00, 35),
('Жёсткий диск WD 2TB', 5990.00, 60),
('SSD Samsung 1TB', 8990.00, 45),
('USB-хаб Anker 7 портов', 2490.00, 80);

-- ============================================
-- ЗАПОЛНЕНИЕ ТАБЛИЦЫ: Покупатели
-- ============================================
INSERT INTO customers (first_name, last_name) VALUES
('Иван', 'Иванов'),
('Петр', 'Петров'),
('Мария', 'Сидорова'),
('Анна', 'Кузнецова'),
('Сергей', 'Смирнов'),
('Ольга', 'Попова'),
('Дмитрий', 'Соколов'),
('Елена', 'Лебедева'),
('Алексей', 'Козлов'),
('Татьяна', 'Новикова');

-- ============================================
-- ЗАПОЛНЕНИЕ ТАБЛИЦЫ: Заказы
-- ============================================
INSERT INTO orders (product_id, customer_id, order_date, quantity) VALUES
(1, 1, '2025-01-15', 1),
(2, 2, '2025-01-16', 2),
(3, 3, '2025-01-17', 1),
(4, 4, '2025-01-18', 3),
(5, 5, '2025-01-19', 2),
(6, 1, '2025-01-20', 1),
(7, 6, '2025-01-21', 4),
(8, 7, '2025-01-22', 1),
(9, 8, '2025-01-23', 2),
(10, 9, '2025-01-24', 5);

-- ============================================
-- КОНЕЦ ФАЙЛА schema.sql
-- ============================================