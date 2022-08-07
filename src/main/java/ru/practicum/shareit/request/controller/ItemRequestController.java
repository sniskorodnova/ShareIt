package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.NoHeaderException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Класс-контроллер для работы с запросами на вещи
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    /**
     * Метод для создания запроса на вещь
     */
    @PostMapping
    public ItemRequestDto create(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                 @Valid @RequestBody ItemRequestCreateDto itemRequestCreate)
            throws UserNotFoundException, NoHeaderException {
        log.debug("Входящий запрос на создание запроса на вещь: " + itemRequestCreate.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestService.create(userId, itemRequestCreate);
        }
    }

    /**
     * Метод для получения списка своих запросов вместе с данными об ответах на них
     */
    @GetMapping()
    public List<ItemRequestDtoWithItems> getByRequesterId(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                                          Long userId)
            throws NoHeaderException, UserNotFoundException, ItemRequestNotFoundException {
        log.debug("Входящий запрос на получение информации по своим запросам для пользователя с id = {}", userId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestService.getByRequesterId(userId);
        }
    }

    /**
     * Метод для получения информации об одном конкретном запросе вместе с данными об ответах на него
     */
    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItems getById(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                               Long userId, @PathVariable Long requestId)
            throws NoHeaderException, UserNotFoundException, ItemRequestNotFoundException {
        log.debug("Входящий запрос на получение информации по запросу с id = {}", requestId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestService.getById(requestId, userId);
        }
    }

    /**
     * Метод для получения списка запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public List<ItemRequestDtoWithItems> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                                Long userId, @RequestParam(required = false, defaultValue = "0")
                                                @Min(0) Integer from,
                                                @RequestParam(required = false, defaultValue = "10")
                                                @Min(1) Integer size)
            throws NoHeaderException, UserNotFoundException, ItemRequestNotFoundException {
        log.debug("Входящий запрос на получение списка запросов, созданных другими пользователями");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestService.getAllWithPagination(userId, from, size);
        }
    }
}
