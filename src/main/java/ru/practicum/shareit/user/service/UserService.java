package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface UserService {
    User create(User user) throws ValidationException;

    User update(Long id, User user) throws ValidationException;

    User getById(Long id);

    void deleteById(Long id);

    List<User> getAll();
}
