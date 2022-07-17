package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getCreated()
        );
    }

    public static Comment toCommentCreate(CommentCreateDto commentCreateDto) {
        return new Comment(
                null,
                commentCreateDto.getText(),
                null,
                null,
                null
        );
    }

    public static CommentForItemDto toCommentForItemDto(Comment comment) {
        return new CommentForItemDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor(),
                comment.getCreated()
        );
    }
}
