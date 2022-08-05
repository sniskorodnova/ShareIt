package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @Valid @RequestBody ItemCreateDto itemCreate) throws NoHeaderException, UserNotFoundException,
            ValidationException {
        log.debug("Входящий запрос на создание вещи: " + itemCreate.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemService.create(userId, itemCreate);
        }
    }

    /**
     * Метод для редактирования вещи
     */
    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @PathVariable Long id, @RequestBody ItemCreateDto itemCreate)
            throws NoHeaderException, ValidationException, AuthFailedException {
        log.debug("Входящий запрос на редактирование вещи: " + itemCreate.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemService.update(userId, id, itemCreate);
        }
    }

    /**
     * Метод для получения вещи по ее id
     */
    @GetMapping("/{id}")
    public ItemDtoWithComment getById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                      @PathVariable Long id) throws NoHeaderException, ItemNotFoundException,
            ValidationException, UserNotFoundException {
        log.debug("Входящий запрос на получение вещи по id = {}", id);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemService.getById(userId, id);
        }
    }

    /**
     * Метод для получения всех вещей
     */
    @GetMapping
    public List<ItemDtoWithComment> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                           @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException, UserNotFoundException {
        log.debug("Входящий запрос на получение всех вещей");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemService.getAllWithPagination(userId, from, size);
        }
    }

    /**
     * Метод для поиска вещей по буквосочетанию
     */
    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                      @RequestParam String text,
                                      @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                      @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException, UserNotFoundException {
        log.debug("Входящий запрос на поиск вещи, содержащую текст: {}", text);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemService.searchByTextWithPagination(userId, text, from, size);
        }
    }

    /**
     * Метод для создания отзыва к вещи
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                    @PathVariable Long itemId, @RequestBody @Valid CommentCreateDto comment)
            throws NoHeaderException, UserNotFoundException, ValidationException {
        log.debug("Входящий запрос на создание отзыва к вещи с id = " + itemId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemService.createComment(userId, itemId, comment);
        }
    }
}
