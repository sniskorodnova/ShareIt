package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, описывающий модель запроса на вещь
 */
@Entity
@Table(name = "item_requests", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(name = "requester_id", nullable = false)
    private Long requesterId;
    private LocalDateTime created;
}
