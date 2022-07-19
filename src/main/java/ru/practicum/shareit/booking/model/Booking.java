package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, описывающий модель бронирования
 */
@Entity
@Table(name = "bookings", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name="item_id", nullable=false)
    private Item item;
    @ManyToOne
    @JoinColumn(name="booker_id", nullable=false)
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
