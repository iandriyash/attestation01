-- АТТЕСТАЦИЯ 02: ТЕСТОВЫЕ ЗАПРОСЫ К БД

-- 1. Получить все товары
SELECT * FROM products;

-- 2. Получить всех покупателей
SELECT * FROM customers;

-- 3. Получить все заказы
SELECT * FROM orders;

-- 4. Получить товары дороже 10000 рублей
SELECT id, description, price
FROM products
WHERE price > 10000
ORDER BY price DESC;

-- 5. Получить заказы с деталями (JOIN трёх таблиц)
SELECT
    o.id AS order_id,
    c.first_name || ' ' || c.last_name AS customer_name,
    p.description AS product_name,
    o.quantity AS ordered_quantity,
    p.price AS unit_price,
    (o.quantity * p.price) AS total_price,
    o.order_date
FROM orders o
JOIN customers c ON o.customer_id = c.id
JOIN products p ON o.product_id = p.id
ORDER BY o.order_date DESC;

-- 6. Посчитать общую сумму заказов для каждого покупателя
SELECT
    c.first_name || ' ' || c.last_name AS customer_name,
    COUNT(o.id) AS total_orders,
    SUM(o.quantity * p.price) AS total_spent
FROM customers c
JOIN orders o ON c.id = o.customer_id
JOIN products p ON o.product_id = p.id
GROUP BY c.id, c.first_name, c.last_name
ORDER BY total_spent DESC;

-- 7. Найти товары, которые заканчиваются (менее 20 штук)
SELECT description, quantity
FROM products
WHERE quantity < 20
ORDER BY quantity ASC;

-- 8. Увеличить цену всех товаров на 5%
UPDATE products
SET price = price * 1.05;

-- 9. Обновить количество товара после продажи
UPDATE products
SET quantity = quantity - 2
WHERE id = 1;

-- 10. Изменить имя покупателя
UPDATE customers
SET first_name = 'Александр', last_name = 'Александров'
WHERE id = 1;

-- 11. Удалить заказ по ID
DELETE FROM orders WHERE id = 10;

-- 12. Удалить покупателя по ID
DELETE FROM customers WHERE id = 10;

-- 13. Удалить товары с нулевым количеством
DELETE FROM products WHERE quantity = 0;