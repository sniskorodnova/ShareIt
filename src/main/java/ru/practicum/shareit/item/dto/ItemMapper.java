package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Класс, описывающий маппинг вещи в модель dto и обратно
 */
public class ItemMapper {
    public static ItemDtoWithComment toItemDtoWithComment(Item item, List<CommentForItemDto> comments,
                                                          BookingDtoItem lastBooking, BookingDtoItem nextBooking) {
        return new ItemDtoWithComment(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId() != null ? item.getRequestId() : null,
                comments,
                lastBooking,
                nextBooking
        );
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId() != null ? item.getRequestId() : null
        );
    }

    public static Item toItemForCreate(ItemCreateDto itemCreateDto) {
        return new Item(
                null,
                itemCreateDto.getName(),
                itemCreateDto.getDescription(),
                itemCreateDto.getAvailable(),
                null,
                null
        );
    }
}
