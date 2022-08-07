package ru.practicum.shareit.request.service;

import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;

import java.util.List;

/**
 * Интерфейс, описывающий логику для работы сервиса запросов на вещи
 */
public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestCreateDto itemRequestCreate) throws UserNotFoundException;

    List<ItemRequestDtoWithItems> getByRequesterId(Long userId) throws UserNotFoundException, ItemRequestNotFoundException;

    ItemRequestDtoWithItems getById(Long requestId, Long userId) throws UserNotFoundException, ItemRequestNotFoundException;

    List<ItemRequestDtoWithItems> getAllWithPagination(Long userId, Integer from, Integer size)
            throws UserNotFoundException, ItemRequestNotFoundException;
}
