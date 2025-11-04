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

### Unit-тесты
- Всего тестов: 55
- Покрытие кода: 32%
- Покрытие Service слоя: 85%
- Покрытие Controller слоя: 96%

Все тесты успешно проходят (BUILD SUCCESS).

### API тестирование через Postman

Протестированы следующие endpoints:

**Pizzas:**
- GET /api/pizzas - получение всех пицц
- GET /api/pizzas/{id} - получение пиццы по ID
- POST /api/pizzas - создание пиццы
- PUT /api/pizzas/{id} - обновление пиццы
- DELETE /api/pizzas/{id} - удаление пиццы
- GET /api/pizzas/search?name= - поиск по названию

**Orders:**
- GET /api/orders - получение всех заказов
- GET /api/orders/{id} - получение заказа по ID
- POST /api/orders - создание заказа
- PATCH /api/orders/{id}/status - обновление статуса
- DELETE /api/orders/{id} - удаление заказа
- GET /api/orders?status= - фильтр по статусу
- GET /api/orders?phone= - фильтр по телефону

## Результаты

Приложение полностью функционально:
- База данных создана и мигрирована через Flyway
- REST API работает корректно
- Soft Delete реализован
- DTO паттерн применён
- Unit-тесты покрывают основную бизнес-логику
- Все требования аттестации выполнены