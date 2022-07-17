package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CommentForItemDto {
    private Long id;
    private String text;
    private User author;
    private LocalDate created;
}
