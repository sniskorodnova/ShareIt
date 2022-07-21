package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.util.List;

/**
 * Класс-dto для отображения информации о вещи с отзывами и ближайшими бронированиями
 */
@Data
@AllArgsConstructor
public class ItemDtoWithComment {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private List<CommentForItemDto> comments;
    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
}
