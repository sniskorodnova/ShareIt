package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

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
    public ItemRequestDto create(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @RequestBody ItemRequestCreateDto itemRequestCreate)
            throws UserNotFoundException {
        return itemRequestService.create(userId, itemRequestCreate);
    }

    /**
     * Метод для получения списка своих запросов вместе с данными об ответах на них
     */
    @GetMapping()
    public List<ItemRequestDtoWithItems> getByRequesterId(@RequestHeader(value = "X-Sharer-User-Id") Long userId)
            throws UserNotFoundException, ItemRequestNotFoundException {
        return itemRequestService.getByRequesterId(userId);
    }

    /**
     * Метод для получения информации об одном конкретном запросе вместе с данными об ответах на него
     */
    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItems getById(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @PathVariable Long requestId)
            throws UserNotFoundException, ItemRequestNotFoundException {
        return itemRequestService.getById(requestId, userId);
    }

    /**
     * Метод для получения списка запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public List<ItemRequestDtoWithItems> getAll(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                @RequestParam Integer from, @RequestParam Integer size)
            throws UserNotFoundException, ItemRequestNotFoundException {
        return itemRequestService.getAllWithPagination(userId, from, size);
    }
}
