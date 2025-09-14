package attestation.attestation01.repositories;

import attestation.attestation01.model.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Файловая реализация интерфейса UsersRepository
 * Обеспечивает сохранение и загрузку пользователей из текстового файла
 * Промежуточная аттестация 01 - Модуль 1 «Java Core»
 */
public class UsersRepositoryFileImpl implements UsersRepository {
    private static final String FILE_PATH = "users.txt";
    private List<User> users = new ArrayList<>();

    public UsersRepositoryFileImpl() {
        loadUsersFromFile();
    }

    @Override
    public void create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        // Проверяем, что ID уникален
        if (users.stream().anyMatch(u -> u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("Пользователь с таким ID уже существует");
        }
        users.add(user);
        saveUsersToFile();
        System.out.println("Пользователь успешно создан с ID: " + user.getId());
    }

    @Override
    public User findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID не может быть пустым");
        }
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Пользователя с заданным идентификатором не существует"));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        if (user.getId() == null || user.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("ID пользователя не может быть пустым");
        }

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Пользователь с ID " + user.getId() + " не найден. Создание нового пользователя.");
            users.add(user);
        }

        saveUsersToFile();
        System.out.println("Пользователь успешно обновлен/создан с ID: " + user.getId());
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID не может быть пустым");
        }

        User userToDelete = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Пользователя с заданным идентификатором не существует"));

        users.remove(userToDelete);
        saveUsersToFile();
        System.out.println("Пользователь с ID " + id + " успешно удален");
    }

    @Override
    public void deleteAll() {
        users.clear();
        saveUsersToFile();
        System.out.println("Все пользователи удалены из системы");
    }

    // Дополнительные методы поиска
    public List<User> findByAge(Integer age) {
        return users.stream()
                .filter(u -> age.equals(u.getAge()))
                .collect(Collectors.toList());
    }

    public List<User> findByIsWorker(boolean isWorker) {
        return users.stream()
                .filter(u -> u.isWorker() == isWorker)
                .collect(Collectors.toList());
    }

    /**
     * Загрузка пользователей из файла при инициализации
     */
    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Файл " + FILE_PATH + " не найден. Будет создан новый файл при первом сохранении.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split("\\|", -1); // -1 для сохранения пустых элементов
                    if (parts.length >= 10) {
                        User user = parseUserFromParts(parts);
                        users.add(user);
                    } else {
                        System.err.println("Некорректная строка " + lineNumber + " в файле: недостаточно полей");
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка парсинга строки " + lineNumber + ": " + e.getMessage());
                }
            }
            System.out.println("Загружено " + users.size() + " пользователей из файла");
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    /**
     * Парсинг пользователя из массива строк
     * @param parts массив полей пользователя
     * @return объект User
     */
    private User parseUserFromParts(String[] parts) {
        String id = parts[0];
        LocalDateTime registrationDate = LocalDateTime.parse(parts[1]);
        String login = parts[2];
        String password = parts[3];
        String confirmPassword = parts[4];
        String lastName = parts[5];
        String firstName = parts[6];
        String middleName = parts[7].isEmpty() ? null : parts[7];
        Integer age = parts[8].isEmpty() ? null : Integer.parseInt(parts[8]);
        boolean isWorker = Boolean.parseBoolean(parts[9]);

        return new User(id, registrationDate, login, password, confirmPassword,
                lastName, firstName, middleName, age, isWorker);
    }

    /**
     * Сохранение всех пользователей в файл
     */
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
            throw new RuntimeException("Не удалось сохранить данные", e);
        }
    }
}