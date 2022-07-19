package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс, описывающий логику для работы сервиса вещей
 */
public interface ItemService {
    Item create(Long userId, Item item) throws ValidationException, UserNotFoundException;

    Item update(Long userId, Long id, Item item) throws ValidationException, AuthFailedException;

    Item getById(Long userId, Long id) throws ValidationException, ItemNotFoundException, UserNotFoundException;

    List<Item> getAll(Long userId) throws ValidationException, UserNotFoundException;

    List<Item> searchByText(Long userId, String searchText) throws UserNotFoundException;

    Comment createComment(Long userId, Long itemId, Comment comment) throws ValidationException, UserNotFoundException;

    List<Comment> getCommentsForItem(Long itemId);

    Booking getLastBooking(Long itemId, Long userId);

    Booking getNextBooking(Long itemId, Long userId);
}
