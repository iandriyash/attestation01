package attestation.attestation01.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

/**
 * Unit-тесты для класса User
 * Промежуточная аттестация 01 - Модуль 1 «Java Core»
 */
public class UserTest {

    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.now();
    }

    // ПОЗИТИВНЫЕ ТЕСТЫ
    @Test
    void testValidUserCreation() {
        User user = new User(
                "test-id-123", testDate, "valid_login", "pass123", "pass123",
                "Иванов", "Иван", "Иванович", 25, true
        );

        assertNotNull(user);
        assertEquals("test-id-123", user.getId());
        assertEquals("valid_login", user.getLogin());
        assertEquals("Иванов", user.getLastName());
        assertEquals("Иван", user.getFirstName());
        assertEquals("Иванович", user.getMiddleName());
        assertEquals(25, user.getAge());
        assertTrue(user.isWorker());

        System.out.println("✓ Тест создания пользователя: ПРОЙДЕН");
    }

    @Test
    void testValidUserCreationWithNullValues() {
        User user = new User(
                "test-id-456", testDate, "user_123", "mypass456", "mypass456",
                "Петров", "Петр", null, null, false
        );

        assertNotNull(user);
        assertEquals("user_123", user.getLogin());
        assertNull(user.getMiddleName());
        assertNull(user.getAge());
        assertFalse(user.isWorker());

        System.out.println("✓ Тест создания пользователя с null-значениями: ПРОЙДЕН");
    }

    @Test
    void testValidLoginWithUnderscoreAndNumbers() {
        User user = new User(
                "id-valid", testDate, "user_name_123", "pass789", "pass789",
                "Сидоров", "Алексей", null, 30, false
        );

        assertEquals("user_name_123", user.getLogin());
        System.out.println("✓ Тест валидного логина с символами: ПРОЙДЕН");
    }

    @Test
    void testValidPasswordWithUnderscore() {
        User user = new User(
                "id-pass", testDate, "username", "my_pass123", "my_pass123",
                "Козлов", "Максим", null, 22, true
        );

        assertEquals("my_pass123", user.getPassword());
        assertEquals("my_pass123", user.getConfirmPassword());
        System.out.println("✓ Тест валидного пароля с символами: ПРОЙДЕН");
    }

    // ТЕСТЫ НА ИСКЛЮЧЕНИЯ
    @Test
    void testInvalidLoginOnlyNumbers() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, "12345", "pass123", "pass123",
                    "Иванов", "Иван", null, 25, false);
        });

        assertTrue(exception.getMessage().contains("не может состоять только из цифр"));
        System.out.println("✓ Тест исключения для числового логина: ПРОЙДЕН");
    }

    @Test
    void testInvalidLoginTooLong() {
        String longLogin = "a".repeat(20);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, longLogin, "pass123", "pass123",
                    "Иванов", "Иван", null, 25, false);
        });

        assertTrue(exception.getMessage().contains("меньше 20 символов"));
        System.out.println("✓ Тест исключения для длинного логина: ПРОЙДЕН");
    }

    @Test
    void testInvalidLoginWithSpecialChars() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, "user@name", "pass123", "pass123",
                    "Иванов", "Иван", null, 25, false);
        });
        System.out.println("✓ Тест исключения для логина со спецсимволами: ПРОЙДЕН");
    }

    @Test
    void testInvalidEmptyLogin() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, "", "pass123", "pass123",
                    "Иванов", "Иван", null, 25, false);
        });

        assertTrue(exception.getMessage().contains("не может быть пустым"));
        System.out.println("✓ Тест исключения для пустого логина: ПРОЙДЕН");
    }

    @Test
    void testInvalidPasswordMismatch() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, "validlogin", "pass123", "different123",
                    "Иванов", "Иван", null, 25, false);
        });

        assertTrue(exception.getMessage().contains("должны совпадать"));
        System.out.println("✓ Тест исключения для несовпадающих паролей: ПРОЙДЕН");
    }

    @Test
    void testInvalidPasswordOnlyLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, "validlogin", "password", "password",
                    "Иванов", "Иван", null, 25, false);
        });
        System.out.println("✓ Тест исключения для пароля без цифр: ПРОЙДЕН");
    }

    @Test
    void testInvalidNameWithNumbers() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, "validlogin", "pass123", "pass123",
                    "Иванов123", "Иван", null, 25, false);
        });
        System.out.println("✓ Тест исключения для имени с цифрами: ПРОЙДЕН");
    }

    @Test
    void testInvalidNegativeAge() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("id", testDate, "validlogin", "pass123", "pass123",
                    "Иванов", "Иван", null, -5, false);
        });
        System.out.println("✓ Тест исключения для отрицательного возраста: ПРОЙДЕН");
    }

    // ТЕСТЫ МЕТОДОВ equals и hashCode
    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("same-id", testDate, "login1", "pass123", "pass123",
                "Иванов", "Иван", null, 25, false);
        User user2 = new User("same-id", testDate, "login2", "pass456", "pass456",
                "Петров", "Петр", null, 30, true);
        User user3 = new User("different-id", testDate, "login1", "pass123", "pass123",
                "Иванов", "Иван", null, 25, false);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);

        System.out.println("✓ Тест equals и hashCode: ПРОЙДЕН");
    }

    @Test
    void testToStringMethod() {
        User user = new User("test-id", testDate, "testlogin", "pass123", "pass123",
                "Тестов", "Тест", "Тестович", 25, true);

        String userString = user.toString();

        assertTrue(userString.contains("test-id"));
        assertTrue(userString.contains("testlogin"));
        assertTrue(userString.contains("Тестов"));
        assertTrue(userString.contains("true"));

        System.out.println("✓ Тест toString: ПРОЙДЕН");
        System.out.println("  Результат: " + userString);
    }
}