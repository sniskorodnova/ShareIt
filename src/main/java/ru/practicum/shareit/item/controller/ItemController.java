package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AuthFailedException;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.NoHeaderException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
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
                          @Valid @RequestBody ItemDto item) throws NoHeaderException, ValidationException {
        log.debug("Входящий запрос на создание вещи" + item.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return ItemMapper.toItemDto(itemService.create(userId, ItemMapper.toItem(item)));
        }
    }

    /**
     * Метод для редактирования вещи
     */
    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @PathVariable Long id, @RequestBody ItemDto item)
            throws NoHeaderException, ValidationException, AuthFailedException {
        log.debug("Входящий запрос на редактирование вещи" + item.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return ItemMapper.toItemDto(itemService.update(userId, id, ItemMapper.toItem(item)));
        }
    }

    /**
     * Метод для получения вещи по ее id
     */
    @GetMapping("/{id}")
    public ItemDto getById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                           @PathVariable Long id) throws NoHeaderException, ValidationException {
        log.debug("Входящий запрос на получение вещи по id = {}", id);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return ItemMapper.toItemDto(itemService.getById(userId, id));
        }
    }

    /**
     * Метод для получения всех вещей
     */
    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId)
            throws NoHeaderException, ValidationException {
        List<ItemDto> listItemDto = new ArrayList<>();
        log.debug("Входящий запрос на получение всех вещей");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            for (Item item : itemService.getAll(userId)) {
                listItemDto.add(ItemMapper.toItemDto(item));
            }
            return listItemDto;
        }
    }

    /**
     * Метод для поиска вещей по буквосочетанию
     */
    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                      @RequestParam String text) throws NoHeaderException, ValidationException {
        log.debug("Входящий запрос на поиск вещи");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            List<Item> foundItems = itemService.searchByText(userId, text);

            List<ItemDto> listItemDto = new ArrayList<>();
            if (foundItems == null) {
                return null;
            } else {
                for (Item item : foundItems) {
                    listItemDto.add(ItemMapper.toItemDto(item));
                }
                return listItemDto;
            }
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUserNotFound(final NoHeaderException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAuthFailed(final AuthFailedException e) {
        return new ErrorResponse(e.getMessage());
    }
}
