# Pizzeria Application

Backend веб-приложения для онлайн-заказа пиццы на Java 17 / Spring Boot 3

Приложение предоставляет REST API для работы с каталогом пицц и заказами. Архитектура — слоистая (MVC): **Controller → Service → Repository → Entity/DTO**, с валидацией данных, централизованной обработкой ошибок, безопасностью и миграциями БД.

---

## Описание проекта

Кратко:

- управление каталогом пицц (CRUD-операции);
- создание и обработка заказов с расчётом итоговой стоимости;
- мягкое удаление записей (soft delete) через `isDeleted` для сохранения истории;
- валидация входных данных и единый формат ошибок;
- авторизация через Spring Security (HTTP Basic + form-login);
- документация API через Swagger UI;
- миграции схемы БД через Flyway;
- комплексное тестирование (unit + smoke).

---

## Технологический стек

**Backend**

- Java 17 (LTS)
- Spring Boot 3.x
    - Spring Web (REST API, встроенный Tomcat)
    - Spring Data JPA (ORM через Hibernate)
    - Spring Security (аутентификация, авторизация)
    - Validation (Bean Validation API)

**База данных**

- PostgreSQL (основная БД)
- H2 (in-memory для тестов)
- Flyway (миграции)
- HikariCP (connection pool)

**Документация и тесты**

- springdoc-openapi (Swagger UI / OpenAPI 3)
- JUnit 5
- Mockito
- MockMvc
- JaCoCo (покрытие кода)

**Сборка и инфраструктура**

- Maven
- Lombok
- Docker, Docker Compose


## Архитектура приложения

Слоистая структура:

┌───────────────────────────────┐
│   Controller (REST API)       │  ← HTTP-запросы
├───────────────────────────────┤
│   Service (бизнес-логика)     │  ← транзакции, валидация
├───────────────────────────────┤
│   Repository (доступ к данным)│  ← Spring Data JPA
├───────────────────────────────┤
│   Entity / DTO                │  ← доменная модель и API-модели
├───────────────────────────────┤
│   PostgreSQL                  │  ← хранение данных
└───────────────────────────────┘
Контроллеры (src/attestation/attestation_finalProject/controller)
PizzaController (/api/pizzas)

GET /api/pizzas — список всех пицц

GET /api/pizzas/{id} — получить пиццу по ID

POST /api/pizzas — создать новую пиццу

PUT /api/pizzas/{id} — обновить пиццу

DELETE /api/pizzas/{id} — мягкое удаление (soft delete)

OrderController (/api/orders)

POST /api/orders — создать заказ из CreateOrderRequest

GET /api/orders — список заказов (опционально: фильтры по статусу/телефону)

GET /api/orders/{id} — получить заказ по ID

PATCH /api/orders/{id}/status — изменить статус заказа

DELETE /api/orders/{id} — мягкое удаление

AuthController

GET /login — страница логина (Thymeleaf)

HomeController

GET / — главная страница (демо / index)

Сервисы (src/attestation/attestation_finalProject/service)
PizzaService

операции с каталогом пицц;

валидация данных;

работа только с активными пиццами (isDeleted = false).

OrderService

сценарий «Оформить заказ»:

валидация входных данных (имя, телефон, quantity > 0);

загрузка пицц по pizzaId из БД;

расчёт lineTotal и totalPrice (price snapshot);

сохранение Order и OrderItem в одной транзакции (@Transactional);

маппинг сущностей в OrderDto.

Репозитории (src/attestation/attestation_finalProject/repository)
PizzaRepository extends JpaRepository<Pizza, Long>

OrderRepository extends JpaRepository<Order, Long>

OrderItemRepository extends JpaRepository<OrderItem, Long>

Пример учёта soft-delete:

java
Копировать код
List<Pizza> findByIsDeletedFalse();
Сущности (src/attestation/attestation_finalProject/entity)
Pizza — id, name, description, price, isDeleted, createdAt

Order — id, customerName, customerPhone, totalPrice, status, isDeleted, createdAt, items

OrderItem — id, order, pizza, quantity, priceSnapshot, lineTotal

DTO-модели (src/attestation/attestation_finalProject/dto)
PizzaDto — данные пиццы в API

OrderItemDto — позиция заказа (pizzaId, name, quantity, price, lineTotal)

OrderDto — заказ (id, status, totalPrice, createdAt, items[])

CreateOrderRequest — входящий запрос (customerName, customerPhone, items[pizzaId, quantity>0])

DTO отделяют внутреннюю модель БД от внешнего API, скрывают служебные поля (isDeleted и т.п.) и позволяют развивать схему без поломки контрактов.

Конфиг и утилиты (src/attestation/attestation_finalProject/config, .../utils, .../exception)
OpenApiConfig — настройка springdoc / Swagger

SecurityConfig — правила доступа, HTTP Basic/Auth, form-login

ValidationUtils — проверки телефонов, имён и пр.

GlobalExceptionHandler, NotFoundException, ValidationException — единый формат ошибок для API

Модель данных
Фактическая структура (как в resources/db.migration):

text
Копировать код
┌──────────────┐        ┌────────────────────┐        ┌──────────────┐
│   pizzas     │        │    order_items     │        │    orders    │
├──────────────┤        ├────────────────────┤        ├──────────────┤
│ id (PK)      │◄───────┤ pizza_id (FK)      │        │ id (PK)      │
│ name         │        │ order_id (FK) ─────┼──────► │ customer_name│
│ description  │        │ quantity           │        │ customer_phone
│ price        │        │ price_snapshot     │        │ total_price  │
│ is_deleted   │        │ line_total         │        │ status       │
│ created_at   │        └────────────────────┘        │ is_deleted   │
└──────────────┘                                      │ created_at   │
                                                      └──────────────┘
Миграции (каталог resources/db.migration/):

V1__create_pizzas_table.sql — таблица pizzas + стартовые записи

V2__create_orders_table.sql — таблица orders (статус по умолчанию NEW)

V3__create_order_items_table.sql — таблица order_items c FK order_id, pizza_id, полями quantity, price_snapshot, line_total

Конфигурация и запуск
Файлы конфигурации (как в проекте)
resources/application.properties — профиль по умолчанию (local)

resources/application-docker.properties — настройки для запуска в Docker

test/resources/application-test.properties — профиль тестов (H2 и тише логи)

Пример application.properties:

properties

spring.datasource.url=jdbc:postgresql://localhost:5433/pizzeria_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false

spring.flyway.enabled=true
# spring.flyway.locations=classpath:db.migration
Локальный запуск
Требования:

Java 17+

Maven

PostgreSQL

(опционально) Docker

1. Создать базу:

sql

CREATE DATABASE pizzeria_db;
2. Проверить настройки в resources/application.properties
(см. пример выше).

3. Собрать и запустить:

bash

mvn clean install
mvn spring-boot:run
Приложение: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui/index.html

Запуск через Docker Compose
Каталог docker/:

docker-compose.yml

Dockerfile

Команды:

bash
Копировать код
# остановка старых контейнеров (если есть)
docker-compose down

# очистка/сборка и запуск
docker-compose up --build
Результат:

PostgreSQL — localhost:5433

Spring Boot — http://localhost:8080

Swagger UI — http://localhost:8080/swagger-ui/index.html

Документация и использование API
Swagger UI:
http://localhost:8080/swagger-ui/index.html

Авторизация: HTTP Basic для /api/**

Тестовые пользователи:

admin / admin123 (ROLE_ADMIN)

user / user123 (ROLE_USER)

Примеры:

GET /api/pizzas — список пицц

http

GET /api/pizzas
Authorization: Basic dXNlcjp1c2VyMTIz
POST /api/orders — создание заказа

http

POST /api/orders
Content-Type: application/json
Authorization: Basic dXNlcjp1c2VyMTIz
json

{
  "customerName": "Иван Иванов",
  "customerPhone": "+79991234567",
  "items": [
    { "pizzaId": 1, "quantity": 2 },
    { "pizzaId": 2, "quantity": 1 }
  ]
}
Тестирование

test/
├─ attestation/              # промежуточная аттестация №1
├─ attestation01.model/      # промежуточная аттестация №2
├─ attestation03/            # промежуточная аттестация №3
├─ attestation_finalProject/ # итоговоая аттестация
│  ├─ controller/
│  │  ├─ OrderControllerSmokeTest.java
│  │  └─ PizzaControllerSmokeTest.java
│  ├─ repository/
│  │  ├─ OrderRepositorySmokeTest.java
│  │  └─ PizzaRepositorySmokeTest.java
│  └─ service/
│     ├─ OrderServiceTest.java
│     └─ PizzaServiceTest.java
└─ resources/
   └─ application-test.properties
Типы тестов
Unit-тесты сервисов (OrderServiceTest, PizzaServiceTest)
Используют Mockito: мок репозиториев, проверка бизнес-инвариантов (расчёт суммы, обработка ошибок, валидация).

Smoke / slice-тесты контроллеров (OrderControllerSmokeTest, PizzaControllerSmokeTest)
Используют MockMvc: проверка HTTP-контрактов (status, структура JSON, ошибки валидации).

Smoke-тесты репозиториев (OrderRepositorySmokeTest, PizzaRepositorySmokeTest)
Проверка запросов к БД, работы с флагом isDeleted на H2 по профилю test.

Запуск тестов:

bash
Копировать код
mvn test
# или с отчётом покрытия
mvn clean test jacoco:report
Отчёт JaCoCo: target/site/jacoco/index.html

Структура проекта (как в IDEA)
text
Копировать код
attestation01/
├─ .idea/
├─ docker/
│  ├─ docker-compose.yml
│  └─ Dockerfile
├─ postman/
│  └─ Pizzeria.postman_collection.json
├─ resources/
│  ├─ db.migration/
│  │  ├─ V1__create_pizzas_table.sql
│  │  ├─ V2__create_orders_table.sql
│  │  └─ V3__create_order_items_table.sql
│  ├─ static.css/                 # стили (кастомное имя каталога)
│  ├─ templates/
│  │  ├─ index.html
│  │  └─ login.html
│  ├─ application.properties
│  ├─ application-docker.properties
│  └─ login.html                   # дополнительный шаблон (legacy/demo)
├─ src/
│  └─ attestation/
│     └─ attestation_finalProject/
│        ├─ config/
│        │  ├─ OpenApiConfig.java
│        │  └─ SecurityConfig.java
│        ├─ controller/
│        │  ├─ AuthController.java
│        │  ├─ HomeController.java
│        │  ├─ OrderController.java
│        │  └─ PizzaController.java
│        ├─ dto/
│        │  ├─ CreateOrderRequest.java
│        │  ├─ OrderDto.java
│        │  ├─ OrderItemDto.java
│        │  └─ PizzaDto.java
│        ├─ entity/
│        │  ├─ Order.java
│        │  ├─ OrderItem.java
│        │  └─ Pizza.java
│        ├─ exception/
│        │  ├─ GlobalExceptionHandler.java
│        │  ├─ NotFoundException.java
│        │  └─ ValidationException.java
│        ├─ repository/
│        │  ├─ OrderItemRepository.java
│        │  ├─ OrderRepository.java
│        │  └─ PizzaRepository.java
│        ├─ service/
│        │  ├─ OrderService.java
│        │  └─ PizzaService.java
│        ├─ utils/
│        │  └─ ValidationUtils.java
│        ├─ Main.java
│        └─ PizzeriaApplication.java
├─ target/                         # артефакты сборки Maven
├─ test/                           # см. структуру тестов выше
│  ├─ attestation/
│  ├─ attestation01.model/
│  ├─ attestation03/
│  ├─ attestation_finalProject/
│  └─ resources/
├─ ATTESTATION03_REPORT.md
├─ README.md
├─ pom.xml
└─ users.txt
Возможные направления развития
JWT-авторизация и личный кабинет пользователя

интеграция с платёжными системами (webhook’и, статусы оплаты)

пагинация и расширенные фильтры для списка заказов

вынесение фронтенда в отдельное SPA-приложение

Testcontainers (PostgreSQL в интеграционных тестах)

мониторинг (Spring Boot Actuator, Prometheus, Grafana)

Автор
Андреев
Итоговая аттестационная работа по курсу «Java-разработчик. Базовый курс»
2025 год