package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public Object create(@RequestBody @Validated UserDto user) {
        log.info("Входящий запрос на создание пользователя" + user.toString());
        return userClient.create(user);
    }

    @PatchMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody UserDto user) {
        log.info("Входящий запрос на создание пользователя" + user.toString());
        return userClient.update(id, user);
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        log.info("Входящий запрос на получение пользователя c id = {}", id);
        return userClient.getById(id);
    }

    @GetMapping
    public Object getAll() {
        log.info("Входящий запрос на получение всех пользователей");
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public Object deleteById(@PathVariable Long id) {
        log.info("Входящий запрос на удаление пользователя c id = {}", id);
        return userClient.deleteById(id);
    }
}
