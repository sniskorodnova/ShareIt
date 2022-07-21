package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

/**
 * Интерфейс, описывающий логику для работы сервиса пользователей
 */
public interface UserService {
    UserDto create(UserDto user) throws ValidationException;

    UserDto update(Long id, UserDto user) throws EmailAlreadyExistsException;

    UserDto getById(Long id) throws ValidationException, UserNotFoundException;

    void deleteById(Long id);

    List<UserDto> getAll();
}
