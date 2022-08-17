package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс-dto, описывающий сущность пользователя
 */
@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
