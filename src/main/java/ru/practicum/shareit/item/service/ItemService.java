package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.AuthFailedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface ItemService {
    Item create(Long userId, Item item) throws ValidationException;

    Item update(Long userId, Long id, Item item) throws ValidationException, AuthFailedException;

    Item getById(Long userId, Long id) throws ValidationException;

    List<Item> getAll(Long userId) throws ValidationException;

    List<Item> searchByText(Long userId, String searchText) throws ValidationException;
}
