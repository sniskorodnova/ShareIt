package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс-dto для создания запроса на вещь
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestCreateDto {
    private String description;
}
