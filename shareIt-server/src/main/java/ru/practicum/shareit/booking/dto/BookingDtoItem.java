package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс-dto для отображения пользователю информации о бронировании в информации о вещи
 */
@Data
@AllArgsConstructor
public class BookingDtoItem {
    private Long id;
    private Long bookerId;
}
