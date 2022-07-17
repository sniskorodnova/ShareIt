package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

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
                          @Valid @RequestBody ItemCreateDto item) throws NoHeaderException, UserNotFoundException,
            ValidationException {
        log.debug("Входящий запрос на создание вещи" + item.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return ItemMapper.toItemDto(itemService.create(userId, ItemMapper.toItemCreate(item)));
        }
    }

    /**
     * Метод для редактирования вещи
     */
    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @PathVariable Long id, @RequestBody ItemCreateDto item)
            throws NoHeaderException, ValidationException, AuthFailedException {
        log.debug("Входящий запрос на редактирование вещи" + item.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return ItemMapper.toItemDto(itemService.update(userId, id, ItemMapper.toItemCreate(item)));
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
            List<CommentForItemDto> comments = new ArrayList<>();
            for (Comment comment : itemService.getCommentsForItem(id)) {
                comments.add(CommentMapper.toCommentForItemDto(comment));
            }
            return ItemMapper.toItemDtoWithComment(itemService.getById(userId, id), comments);
        }
    }

    /**
     * Метод для получения всех вещей
     */
    @GetMapping
    public List<ItemDtoWithComment> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId)
            throws NoHeaderException, UserNotFoundException, ValidationException, ItemNotFoundException {
        log.debug("Входящий запрос на получение всех вещей");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            List<ItemDtoWithComment> listItemCommentDto = new ArrayList<>();
            for (Item item : itemService.getAll(userId)) {
                List<CommentForItemDto> comments = new ArrayList<>();
                for (Comment comment : itemService.getCommentsForItem(item.getId())) {
                    comments.add(CommentMapper.toCommentForItemDto(comment));
                }
                listItemCommentDto.add(ItemMapper.toItemDtoWithComment(itemService.getById(userId, item.getId()),
                        comments));
            }
            return listItemCommentDto;
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

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                    @PathVariable Long itemId, @RequestBody CommentCreateDto comment)
            throws NoHeaderException, UserNotFoundException, ValidationException {
        log.debug("Входящий запрос на создание отзыва к вещи" + itemId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return CommentMapper.toCommentDto(itemService.createComment(userId, itemId,
                    CommentMapper.toCommentCreate(comment)));
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUserNotFound(final NoHeaderException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFound(final ItemNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAuthFailed(final AuthFailedException e) {
        return new ErrorResponse(e.getMessage());
    }
}
