package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * Класс-dto для отображения информации об отзыве в ответе на получение информации о вещи
 */
@Data
@AllArgsConstructor
public class CommentForItemDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDate created;
}
