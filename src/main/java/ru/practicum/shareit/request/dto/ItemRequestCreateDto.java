package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Класс-dto для создания запроса на вещь
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestCreateDto {
    @NotEmpty
    private String description;
}
