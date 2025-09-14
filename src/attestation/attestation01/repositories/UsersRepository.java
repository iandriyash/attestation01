package attestation.attestation01.repositories;

import attestation.attestation01.model.User;

import java.util.List;

/**
 * Интерфейс для работы с пользователями
 * Определяет основные операции CRUD для управления пользователями
 * Промежуточная аттестация 01 - Модуль 1 «Java Core»
 */
public interface UsersRepository {

    /**
     * Создание пользователя и запись его в файл
     * @param user пользователь для создания
     */
    void create(User user);

    /**
     * Поиск пользователя в файле по идентификатору
     * @param id идентификатор пользователя
     * @return найденный пользователь
     * @throws IllegalArgumentException если пользователь не найден
     */
    User findById(String id);

    /**
     * Выгрузка всех пользователей из файла
     * @return список всех пользователей
     */
    List<User> findAll();

    /**
     * Обновление полей существующего в файле пользователя
     * @param user пользователь с обновленными данными
     */
    void update(User user);

    /**
     * Удаление пользователя по идентификатору
     * @param id идентификатор пользователя для удаления
     * @throws IllegalArgumentException если пользователь не найден
     */
    void deleteById(String id);

    /**
     * Удаление всех пользователей
     */
    void deleteAll();
}