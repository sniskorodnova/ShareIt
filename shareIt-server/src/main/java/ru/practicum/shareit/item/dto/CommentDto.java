package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

/**
 * Класс-dto для отображения информации об отзыве
 */
@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Item item;
    private String authorName;
    private LocalDate created;
}
