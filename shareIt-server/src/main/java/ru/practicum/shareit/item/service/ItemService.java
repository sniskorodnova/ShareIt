package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

/**
 * Интерфейс, описывающий логику для работы сервиса вещей
 */
public interface ItemService {
    ItemDto create(Long userId, ItemCreateDto itemCreate) throws ValidationException, UserNotFoundException;

    ItemDto update(Long userId, Long id, ItemCreateDto itemCreate) throws ValidationException, AuthFailedException;

    ItemDtoWithComment getById(Long userId, Long id) throws ValidationException, ItemNotFoundException,
            UserNotFoundException;

    List<ItemDtoWithComment> getAllWithPagination(Long userId, Integer from, Integer size) throws UserNotFoundException;

    List<ItemDto> searchByTextWithPagination(Long userId, String searchText, Integer from, Integer size)
            throws UserNotFoundException;

    CommentDto createComment(Long userId, Long itemId, CommentCreateDto commentCreate) throws ValidationException,
            UserNotFoundException;
}
