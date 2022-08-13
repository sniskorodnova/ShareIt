package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс-dto для создания вещи
 */
@Data
@AllArgsConstructor
public class ItemCreateDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
