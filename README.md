# Pizzeria Application

Backend веб-приложения для онлайн-заказа пиццы на Java 17 / Spring Boot 3.

Приложение предоставляет REST API для работы с каталогом пицц и заказами. Архитектура — слоистая (MVC): **Controller → Service → Repository → Entity/DTO**, с валидацией данных, централизованной обработкой ошибок, безопасностью и миграциями БД.

---

## Описание проекта

- управление каталогом пицц (CRUD-операции);
- создание и обработка заказов с расчётом итоговой стоимости;
- мягкое удаление записей (soft delete) через поле `isDeleted` для сохранения истории;
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

---

## Архитектура приложения

Слоистая структура:

```text
┌───────────────────────────────┐
│ Controller (REST API)         │  ← HTTP-запросы
├───────────────────────────────┤
│ Service (бизнес-логика)       │  ← транзакции, валидация
├───────────────────────────────┤
│ Repository (доступ к данным)  │  ← Spring Data JPA
├───────────────────────────────┤
│ Entity / DTO                  │  ← доменная модель и API-модели
├───────────────────────────────┤
│ PostgreSQL                    │  ← хранение данных
└───────────────────────────────┘
