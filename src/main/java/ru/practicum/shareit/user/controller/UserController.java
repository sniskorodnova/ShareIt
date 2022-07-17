package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-контроллер для работы с пользователями
 */
@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    /**
     * Метод для создания пользователя
     */
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) throws ValidationException {
        log.debug("Входящий запрос на создание пользователя" + user.toString());
        return UserMapper.toUserDto(userService.create(UserMapper.toUser(user)));
    }

    /**
     * Метод для редактирования пользователя
     */
    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto user) throws ValidationException {
        log.debug("Входящий запрос на редактирование пользователя" + user.toString());
        return UserMapper.toUserDto(userService.update(id, UserMapper.toUser(user)));
    }

    /**
     * Метод для получения пользователя по id
     */
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) throws UserNotFoundException, ValidationException {
        log.debug("Входящий запрос на получение пользователя c id = {}", id);
        return UserMapper.toUserDto(userService.getById(id));
    }

    /**
     * Метод для получения списка всех пользователей
     */
    @GetMapping
    public List<UserDto> getAll() {
        List<UserDto> listUserDto = new ArrayList<>();
        log.debug("Входящий запрос на получение всех пользователей");
        for (User user : userService.getAll()) {
            listUserDto.add(UserMapper.toUserDto(user));
        }
        return listUserDto;
    }

    /**
     * Метод для удаления пользователя по id
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.debug("Входящий запрос на удаление пользователя c id = {}", id);
        userService.deleteById(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}
