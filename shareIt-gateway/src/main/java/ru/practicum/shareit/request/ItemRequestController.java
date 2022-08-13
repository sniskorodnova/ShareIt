package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NoHeaderException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public Object create(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                         @RequestBody @Valid ItemRequestCreateDto itemRequestCreate) throws NoHeaderException {
        log.debug("Входящий запрос на создание запроса на вещь: " + itemRequestCreate.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestClient.create(userId, itemRequestCreate);
        }
    }

    @GetMapping
    public Object getByRequesterId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId)
            throws NoHeaderException {
        log.debug("Входящий запрос на получение информации по своим запросам для пользователя с id = {}", userId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestClient.getByRequesterId(userId);
        }
    }

    @GetMapping("/{requestId}")
    public Object getById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @PathVariable Long requestId)
            throws NoHeaderException {
        log.debug("Входящий запрос на получение информации по запросу с id = {}", requestId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestClient.getById(userId,requestId);
        }
    }

    @GetMapping("/all")
    public Object getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                         Long userId, @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                         @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException {
        log.debug("Входящий запрос на получение списка запросов, созданных другими пользователями");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemRequestClient.getAll(userId, from, size);
        }
    }
}
