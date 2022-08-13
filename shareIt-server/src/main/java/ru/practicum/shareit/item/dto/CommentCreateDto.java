package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс-dto для создания отзыва
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {
    private String text;
}
