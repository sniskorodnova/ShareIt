package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * Класс-контроллер для работы с вещами
 */
@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    /**
     * Метод для создания вещи
     */
    @PostMapping
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @RequestBody ItemCreateDto itemCreate)
            throws UserNotFoundException, ValidationException {
        return itemService.create(userId, itemCreate);
    }

    /**
     * Метод для редактирования вещи
     */
    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @PathVariable Long id,
                          @RequestBody ItemCreateDto itemCreate)
            throws ValidationException, AuthFailedException {
        return itemService.update(userId, id, itemCreate);
    }

    /**
     * Метод для получения вещи по ее id
     */
    @GetMapping("/{id}")
    public ItemDtoWithComment getById(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                      @PathVariable Long id) throws ItemNotFoundException, ValidationException,
            UserNotFoundException {
        return itemService.getById(userId, id);
    }

    /**
     * Метод для получения всех вещей
     */
    @GetMapping
    public List<ItemDtoWithComment> getAll(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @RequestParam Integer from, @RequestParam Integer size)
            throws UserNotFoundException {
        return itemService.getAllWithPagination(userId, from, size);
    }

    /**
     * Метод для поиска вещей по буквосочетанию
     */
    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                      @RequestParam String text, @RequestParam Integer from, @RequestParam Integer size)
            throws UserNotFoundException {
        return itemService.searchByTextWithPagination(userId, text, from, size);
    }

    /**
     * Метод для создания отзыва к вещи
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId, @RequestBody CommentCreateDto comment)
            throws UserNotFoundException, ValidationException {
        return itemService.createComment(userId, itemId, comment);
    }
}
