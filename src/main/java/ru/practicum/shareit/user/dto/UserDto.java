package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Класс-dto, описывающий сущность пользователя
 */
@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @NotEmpty
    @Email
    private String email;
}
