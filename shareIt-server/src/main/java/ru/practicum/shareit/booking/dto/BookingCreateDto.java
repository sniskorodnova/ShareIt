package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс-dto для создания бронирования
 */
@Data
@AllArgsConstructor
public class BookingCreateDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
