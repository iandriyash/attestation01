# Аттестация 03 - Spring Boot REST API для Пиццерии

## Описание проекта
REST API для управления пиццерией с использованием Spring Boot, PostgreSQL и Flyway.

## Структура базы данных
- pizzas (id, name, description, price, is_deleted)
- orders (id, customer_name, customer_phone, total_price, status, created_at, is_deleted)
- order_items (id, order_id, pizza_id, quantity, price)

## Реализованные endpoints

### Pizzas
- GET /api/pizzas - Получить все пиццы
- GET /api/pizzas/{id} - Получить пиццу по ID
- POST /api/pizzas - Создать пиццу
- PUT /api/pizzas/{id} - Обновить пиццу
- DELETE /api/pizzas/{id} - Удалить пиццу (soft delete)

### Orders
- GET /api/orders - Получить все заказы
- GET /api/orders/{id} - Получить заказ по ID
- POST /api/orders - Создать заказ
- PATCH /api/orders/{id}/status - Обновить статус заказа
- DELETE /api/orders/{id} - Удалить заказ (soft delete)

## Технологии
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL 16
- Flyway
- Lombok
- Maven

## Тестирование
API протестирован через Postman. Все endpoints работают корректно.