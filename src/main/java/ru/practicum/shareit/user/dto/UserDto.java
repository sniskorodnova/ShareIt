package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @NotEmpty(message = "Email must not be empty")
    @Email
    private String email;
}
