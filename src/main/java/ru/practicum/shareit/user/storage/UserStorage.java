package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс, описывающий логику сохранения данных для сущностей пользователей
 */
public interface UserStorage {
    User create(User user);

    User update(User user);

    User getById(Long id);

    void deleteById(Long id);

    List<User> getAll();
}
