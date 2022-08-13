package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс-dto для отображения информации о вещи
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
