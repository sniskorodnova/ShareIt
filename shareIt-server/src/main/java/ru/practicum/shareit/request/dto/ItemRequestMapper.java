package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Класс, описывающий маппинг запроса на вещь в модели dto и обратно
 */
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toItemRequestForCreate(ItemRequestCreateDto itemRequestCreateDto) {
        return new ItemRequest(
                null,
                itemRequestCreateDto.getDescription(),
                null,
                null
        );
    }

    public static ItemRequestDtoWithItems toItemRequestDtoWithItems(ItemRequest itemRequest, List<ItemDto> items) {
        return new ItemRequestDtoWithItems(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }
}
