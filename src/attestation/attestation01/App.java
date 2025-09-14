package attestation.attestation01;

import attestation.attestation01.model.User;
import attestation.attestation01.repositories.UsersRepositoryFileImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Главный класс приложения для тестирования функционала управления пользователями
 * Промежуточная аттестация 01 - Модуль 1 «Java Core»
 * Структура: attestation/attestation01
 */
public class App {
    public static void main(String[] args) {
        System.out.println("=== ПРОМЕЖУТОЧНАЯ АТТЕСТАЦИЯ 01 ===");
        System.out.println("Модуль 1 «Java Core»");
        System.out.println("Пакет: attestation.attestation01");
        System.out.println("Система управления пользователями");
        System.out.println("Ветка Git: attestation/attestation01\n");

        // Создание начального файла с тестовыми данными
        initializeUsersFile();

        // Создание экземпляра репозитория
        UsersRepositoryFileImpl repository = new UsersRepositoryFileImpl();

        System.out.println("\n=== ТЕСТИРОВАНИЕ ФУНКЦИОНАЛА UsersRepositoryFileImpl ===\n");

        // Выполнение всех тестов согласно заданию
        testCreate(repository);
        testFindById(repository);
        testFindAll(repository);
        testUpdate(repository);
        testDeleteById(repository);
        testAdditionalMethods(repository);
        testDeleteAll(repository);

        System.out.println("=== АТТЕСТАЦИЯ 01 ЗАВЕРШЕНА УСПЕШНО ===");
        System.out.println("✓ Все методы UsersRepositoryFileImpl протестированы");
        System.out.println("✓ Функционал работы с пользователями реализован");
        System.out.println("✓ Обработка ошибок чтения/записи файла выполнена");
    }

    /**
     * Создание исходного файла с набором пользователей согласно заданию
     */
    private static void initializeUsersFile() {
        System.out.println("Создание исходного файла users.txt с набором пользователей...");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            // Исходный пользователь из задания (точный формат)
            writer.write("f5a8a3cb-4ac9-4b3b-8a65-c424e129b9d2|2023-12-25T19:10:11.556|noisemc_99|789ghs|789ghs|Крылов|Виктор|Павлович|25|true");
            writer.newLine();

            // Дополнительные тестовые пользователи для проверки функционала
            writer.write("a1b2c3d4-e5f6-4g7h-8i9j-k0l1m2n3o4p5|2023-12-26T09:15:22|user_john|pass123|pass123|Иванов|Иван||30|false");
            writer.newLine();

            writer.write("b2c3d4e5-f6g7-5h8i-9j0k-l1m2n3o4p5q6|2024-01-15T14:30:45|worker_anna|admin456|admin456|Петрова|Анна|Сергеевна||true");
            writer.newLine();

            System.out.println("✓ Исходный файл users.txt создан успешно");
        } catch (IOException e) {
            System.err.println("✗ Ошибка создания исходного файла: " + e.getMessage());
        }
    }

    /**
     * Тест создания пользователя - метод create()
     */
    private static void testCreate(UsersRepositoryFileImpl repository) {
        System.out.println("1. === ТЕСТ: void create(User user) ===");
        try {
            User newUser = new User(
                    "x7y8z9a0-b1c2-3d4e-5f6g-7h8i9j0k1l2m",
                    LocalDateTime.now(),
                    "test_login",
                    "mypass123",
                    "mypass123",
                    "Сидоров",
                    "Алексей",
                    "Петрович",
                    28,
                    true
            );

            repository.create(newUser);
            System.out.println("✓ Создание пользователя: УСПЕШНО");
            System.out.println("  ID: " + newUser.getId());
            System.out.println("  Логин: " + newUser.getLogin());
            System.out.println("  ФИО: " + newUser.getLastName() + " " + newUser.getFirstName() + " " + newUser.getMiddleName());
            System.out.println("  Сотрудник: " + (newUser.isWorker() ? "Да" : "Нет"));

        } catch (Exception e) {
            System.err.println("✗ Создание пользователя: ОШИБКА - " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Тест поиска пользователя по ID - метод findById()
     */
    private static void testFindById(UsersRepositoryFileImpl repository) {
        System.out.println("2. === ТЕСТ: User findById(String id) ===");

        // Тест успешного поиска
        try {
            String searchId = "f5a8a3cb-4ac9-4b3b-8a65-c424e129b9d2";
            User foundUser = repository.findById(searchId);
            System.out.println("✓ Поиск существующего пользователя: УСПЕШНО");
            System.out.println("  ID: " + foundUser.getId());
            System.out.println("  Логин: " + foundUser.getLogin());
            System.out.println("  ФИО: " + foundUser.getLastName() + " " + foundUser.getFirstName() +
                    (foundUser.getMiddleName() != null ? " " + foundUser.getMiddleName() : ""));
            System.out.println("  Возраст: " + foundUser.getAge());
        } catch (Exception e) {
            System.err.println("✗ Поиск существующего пользователя: ОШИБКА - " + e.getMessage());
        }

        // Тест поиска несуществующего пользователя (должно выбросить исключение)
        try {
            repository.findById("nonexistent-id-12345");
            System.err.println("✗ Поиск несуществующего пользователя: ОШИБКА - исключение не выброшено");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Поиск несуществующего пользователя: УСПЕШНО");
            System.out.println("  Исключение корректно выброшено: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Тест получения всех пользователей - метод findAll()
     */
    private static void testFindAll(UsersRepositoryFileImpl repository) {
        System.out.println("3. === ТЕСТ: List<User> findAll() ===");
        try {
            List<User> allUsers = repository.findAll();
            System.out.println("✓ Выгрузка всех пользователей: УСПЕШНО");
            System.out.println("  Общее количество: " + allUsers.size());

            for (int i = 0; i < allUsers.size(); i++) {
                User user = allUsers.get(i);
                System.out.printf("  %d. %s %s (%s) - %s - %s лет%n",
                        i + 1,
                        user.getFirstName(),
                        user.getLastName(),
                        user.getLogin(),
                        user.isWorker() ? "Сотрудник" : "Пользователь",
                        user.getAge() != null ? user.getAge() : "?"
                );
            }
        } catch (Exception e) {
            System.err.println("✗ Выгрузка всех пользователей: ОШИБКА - " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Тест обновления пользователя - метод update()
     */
    private static void testUpdate(UsersRepositoryFileImpl repository) {
        System.out.println("4. === ТЕСТ: void update(User user) ===");
        try {
            // Получаем существующего пользователя и обновляем его данные
            String updateId = "f5a8a3cb-4ac9-4b3b-8a65-c424e129b9d2";
            User existingUser = repository.findById(updateId);

            System.out.println("  Данные до обновления:");
            System.out.println("    Логин: " + existingUser.getLogin());
            System.out.println("    Возраст: " + existingUser.getAge());
            System.out.println("    Сотрудник: " + existingUser.isWorker());

            User updatedUser = new User(
                    existingUser.getId(),
                    existingUser.getRegistrationDate(),
                    "updated_login",
                    "newpass456",
                    "newpass456",
                    existingUser.getLastName(),
                    existingUser.getFirstName(),
                    existingUser.getMiddleName(),
                    26, // обновляем возраст
                    false // меняем статус работника
            );

            repository.update(updatedUser);

            // Проверяем обновление
            User verifyUser = repository.findById(updateId);
            System.out.println("✓ Обновление пользователя: УСПЕШНО");
            System.out.println("  Данные после обновления:");
            System.out.println("    Логин: " + verifyUser.getLogin());
            System.out.println("    Возраст: " + verifyUser.getAge());
            System.out.println("    Сотрудник: " + verifyUser.isWorker());

        } catch (Exception e) {
            System.err.println("✗ Обновление пользователя: ОШИБКА - " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Тест удаления пользователя по ID - метод deleteById()
     */
    private static void testDeleteById(UsersRepositoryFileImpl repository) {
        System.out.println("5. === ТЕСТ: void deleteById(String id) ===");
        try {
            String deleteId = "a1b2c3d4-e5f6-4g7h-8i9j-k0l1m2n3o4p5";

            // Проверяем наличие пользователя перед удалением
            User userBeforeDelete = repository.findById(deleteId);
            System.out.println("  Пользователь найден: " + userBeforeDelete.getLogin());

            // Удаляем пользователя
            repository.deleteById(deleteId);
            System.out.println("✓ Удаление пользователя: УСПЕШНО");

            // Проверяем, что пользователь действительно удален
            try {
                repository.findById(deleteId);
                System.err.println("✗ Проверка удаления: ОШИБКА - пользователь не удален");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Проверка удаления: УСПЕШНО");
                System.out.println("  Пользователь удален из системы");
            }

        } catch (Exception e) {
            System.err.println("✗ Удаление пользователя: ОШИБКА - " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Тест дополнительных методов поиска
     */
    private static void testAdditionalMethods(UsersRepositoryFileImpl repository) {
        System.out.println("6. === ТЕСТ: Дополнительные методы поиска ===");

        // Поиск сотрудников
        try {
            List<User> workers = repository.findByIsWorker(true);
            System.out.println("✓ Поиск сотрудников: УСПЕШНО");
            System.out.println("  Найдено сотрудников: " + workers.size());
            workers.forEach(user ->
                    System.out.println("    " + user.getFirstName() + " " + user.getLastName() + " (" + user.getLogin() + ")")
            );
        } catch (Exception e) {
            System.err.println("✗ Поиск сотрудников: ОШИБКА - " + e.getMessage());
        }

        // Поиск по возрасту
        try {
            List<User> ageGroup = repository.findByAge(26);
            System.out.println("✓ Поиск по возрасту (26 лет): УСПЕШНО");
            System.out.println("  Найдено пользователей: " + ageGroup.size());
            ageGroup.forEach(user ->
                    System.out.println("    " + user.getFirstName() + " " + user.getLastName() + " (возраст: " + user.getAge() + ")")
            );
        } catch (Exception e) {
            System.err.println("✗ Поиск по возрасту: ОШИБКА - " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Тест удаления всех пользователей - метод deleteAll()
     */
    private static void testDeleteAll(UsersRepositoryFileImpl repository) {
        System.out.println("7. === ТЕСТ: void deleteAll() ===");

        try {
            int countBefore = repository.findAll().size();
            System.out.println("  Пользователей до удаления: " + countBefore);

            repository.deleteAll();

            int countAfter = repository.findAll().size();
            System.out.println("✓ Удаление всех пользователей: УСПЕШНО");
            System.out.println("  Пользователей после удаления: " + countAfter);

            if (countAfter == 0) {
                System.out.println("  Все пользователи успешно удалены из системы и файла");
            } else {
                System.err.println("  ОШИБКА: остались пользователи в системе");
            }

        } catch (Exception e) {
            System.err.println("✗ Удаление всех пользователей: ОШИБКА - " + e.getMessage());
        }

        System.out.println();
    }
}