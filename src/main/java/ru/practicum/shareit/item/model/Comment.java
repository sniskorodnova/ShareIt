package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Класс, описывающий модель отзыва
 */
@Entity
@Table(name = "comments", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name="item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private User author;
    @Column(nullable = false)
    private LocalDate created;
}
