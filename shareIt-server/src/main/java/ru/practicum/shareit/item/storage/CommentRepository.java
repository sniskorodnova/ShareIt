package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Репозиторий для работы с отзывами
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItem_id(Long itemId);
}
