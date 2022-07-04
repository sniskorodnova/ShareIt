package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Класс-dto, описывающий сущность вещь
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotEmpty(message = "Name must not be empty")
    private String name;
    @NotEmpty(message = "Description must not be empty")
    private String description;
    @NotNull(message = "Availability must not be empty")
    private Boolean available;
    private Long requestId;
}
