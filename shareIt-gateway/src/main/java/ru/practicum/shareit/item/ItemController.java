package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NoHeaderException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public Object create(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                         @RequestBody @Valid ItemCreateDto itemCreate) throws NoHeaderException {
        log.debug("Входящий запрос на создание вещи: " + itemCreate.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemClient.create(userId, itemCreate);
        }
    }

    @PatchMapping("/{itemId}")
    public Object update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                         @PathVariable Long itemId, @RequestBody ItemCreateDto itemCreate) throws NoHeaderException {
        log.debug("Входящий запрос на редактирование вещи: " + itemCreate.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemClient.update(userId, itemId, itemCreate);
        }
    }

    @GetMapping("/{itemId}")
    public Object getById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @PathVariable Long itemId) throws NoHeaderException {
        log.debug("Входящий запрос на получение вещи по id = {}", itemId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemClient.getById(userId, itemId);
        }
    }

    @GetMapping
    public Object getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                         @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                         @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException {
        log.debug("Входящий запрос на получение всех вещей");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemClient.getAll(userId, from, size);
        }
    }

    @GetMapping("/search")
    public Object searchByText(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                               @RequestParam String text,
                               @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                               @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException {
        log.debug("Входящий запрос на поиск вещи, содержащую текст: {}", text);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemClient.searchByText(userId, text, from, size);
        }
    }

    @PostMapping("/{itemId}/comment")
    public Object createComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                @PathVariable Long itemId, @RequestBody @Valid CommentCreateDto comment)
            throws NoHeaderException {
        log.debug("Входящий запрос на создание отзыва к вещи с id = " + itemId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return itemClient.createComment(userId, itemId, comment);
        }
    }
}
