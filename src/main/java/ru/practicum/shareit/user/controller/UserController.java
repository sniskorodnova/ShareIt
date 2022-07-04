package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) throws ValidationException {
        log.debug("Входящий запрос на создание пользователя");
        log.debug(user.toString());
        return UserMapper.toUserDto(userService.create(UserMapper.toUser(user)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto user) throws ValidationException {
        log.debug("Входящий запрос на редактирование пользователя");
        log.debug(user.toString());
        return UserMapper.toUserDto(userService.update(id, UserMapper.toUser(user)));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        log.debug("Входящий запрос на получение пользователя c id = {}", id);
        return UserMapper.toUserDto(userService.getById(id));
    }

    @GetMapping
    public List<UserDto> getAll(){
        List<UserDto> listUserDto = new ArrayList<>();
        log.debug("Входящий запрос на получение всех пользователей");
        for (User user : userService.getAll()) {
            listUserDto.add(UserMapper.toUserDto(user));
        }
        return listUserDto;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        log.debug("Входящий запрос на удаление пользователя c id = {}", id);
        userService.deleteById(id);
    }
}
