package attestation.attestation01.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс пользователя системы
 * Промежуточная аттестация 01 - Модуль 1 «Java Core»
 */
public class User {
    private String id;
    private LocalDateTime registrationDate;
    private String login;
    private String password;
    private String confirmPassword;
    private String lastName;
    private String firstName;
    private String middleName;
    private Integer age;
    private boolean isWorker;

    // Конструктор по умолчанию
    public User() {
        this.id = UUID.randomUUID().toString();
        this.registrationDate = LocalDateTime.now();
        this.isWorker = false;
    }

    // Полный конструктор
    public User(String id, LocalDateTime registrationDate, String login, String password,
                String confirmPassword, String lastName, String firstName, String middleName,
                Integer age, boolean isWorker) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.registrationDate = registrationDate != null ? registrationDate : LocalDateTime.now();
        setLogin(login);
        setPassword(password);
        setConfirmPassword(confirmPassword);
        setLastName(lastName);
        setFirstName(firstName);
        setMiddleName(middleName);
        setAge(age);
        this.isWorker = isWorker;
    }

    // Валидация логина
    private void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (login.length() >= 20) {
            throw new IllegalArgumentException("Логин должен быть меньше 20 символов");
        }
        if (login.matches("\\d+")) {
            throw new IllegalArgumentException("Логин не может состоять только из цифр");
        }
        if (!login.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Логин может содержать только буквы, цифры и знак подчеркивания");
        }
    }

    // Валидация пароля
    private void validatePassword(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            throw new IllegalArgumentException("Пароль и подтверждение не могут быть пустыми");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Пароль и подтверждение должны совпадать");
        }
        if (password.length() >= 20) {
            throw new IllegalArgumentException("Пароль должен быть меньше 20 символов");
        }
        if (password.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Пароль не может состоять только из букв");
        }
        if (!password.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Пароль может содержать только буквы, цифры и знак подчеркивания");
        }
    }

    // Валидация имени
    private void validateName(String name, String fieldName) {
        if (name != null && !name.trim().isEmpty() && !name.matches("[а-яА-Яa-zA-Z]+")) {
            throw new IllegalArgumentException(fieldName + " должно состоять только из букв");
        }
    }

    // Геттеры
    public String getId() { return id; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public Integer getAge() { return age; }
    public boolean isWorker() { return isWorker; }

    // Сеттеры с валидацией
    public void setId(String id) { this.id = id; }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setLogin(String login) {
        validateLogin(login);
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
        if (this.confirmPassword != null) {
            validatePassword(password, this.confirmPassword);
        }
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        if (this.password != null) {
            validatePassword(this.password, confirmPassword);
        }
    }

    public void setLastName(String lastName) {
        validateName(lastName, "Фамилия");
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        validateName(firstName, "Имя");
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        if (middleName != null && !middleName.trim().isEmpty()) {
            validateName(middleName, "Отчество");
        }
        this.middleName = middleName;
    }

    public void setAge(Integer age) {
        if (age != null && age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }
        this.age = age;
    }

    public void setWorker(boolean worker) {
        this.isWorker = worker;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%b",
                id, registrationDate, login, password, confirmPassword,
                lastName, firstName,
                middleName != null ? middleName : "",
                age != null ? age : "",
                isWorker);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}