package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

/**
 * Интерфейс, описывающий логику для работы сервиса пользователей
 */
public interface UserService {
    User create(User user) throws ValidationException;

    User update(Long id, User user) throws ValidationException;

    User getById(Long id) throws ValidationException, UserNotFoundException;

    void deleteById(Long id);

    List<User> getAll();
}
