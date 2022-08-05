package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Класс-контроллер для работы с бронированиями
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    /**
     * Метод для создания бронирования
     */
    @PostMapping
    public BookingDto create(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                             @Valid @RequestBody BookingCreateDto booking) throws ItemNotFoundException,
            UserNotFoundException, ValidationException, NoHeaderException {
        log.debug("Входящий запрос на создание запроса на бронирование: " + booking.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingService.create(userId, booking);
        }
    }

    /**
     * Метод для изменения статуса бронирования. Изменять статус может только владелец вещи
     */
    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                             @PathVariable Long bookingId, @RequestParam Boolean approved) throws NoHeaderException,
            UserNotFoundException, BookingNotFoundException, ItemNotBelongsToUserException, ValidationException {
        log.debug("Входящий запрос на редактирование статуса запроса на бронирование");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingService.update(userId, bookingId, approved);
        }
    }

    /**
     * Метод для получения информации о бронировании
     */
    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                              @PathVariable Long bookingId) throws NoHeaderException,
            UserNotFoundException, BookingNotFoundException, ItemNotBelongsToUserException {
        log.debug("Входящий запрос на получение информации по бронированию с id = {}", bookingId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingService.getById(userId, bookingId);
        }
    }

    /**
     * Метод для получения всех бронирований пользователя в определенном статусе, запрошенном клиентом
     */
    @GetMapping()
    public List<BookingDto> getAllBookingsForRequester(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                                       Long userId, @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(required = false, defaultValue = "0")
                                                       @Min(0) Integer from,
                                                       @RequestParam(required = false, defaultValue = "10")
                                                       @Min(1) Integer size)
            throws NoHeaderException, UserNotFoundException, UnsupportedStatusException {
        try {
            State stateToConvert = State.valueOf(state);
            if (userId == null) {
                throw new NoHeaderException("No header in the request");
            } else {
                log.debug("Входящий запрос на получение информации по всем бронированиям для пользователю {}", userId);
                return bookingService.getAllBookingsForRequesterWithPagination(userId, stateToConvert, from, size);
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }

    /**
     * Метод для получения списка бронирований для всех вещей пользователя, запрошенного с определенным статусом
     * от клиента
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                                   Long userId, @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(required = false, defaultValue = "0")
                                                   @Min(0) Integer from,
                                                   @RequestParam(required = false, defaultValue = "10")
                                                   @Min(1) Integer size)
            throws NoHeaderException,UserNotFoundException, UnsupportedStatusException {
        try {
            State stateToConvert = State.valueOf(state);
            if (userId == null) {
                throw new NoHeaderException("No header in the request");
            } else {
                log.debug("Входящий запрос на получение информации по всем бронированиям для владельца {}", userId);
                return bookingService.getAllBookingsForOwnerWithPagination(userId, stateToConvert, from, size);
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }
}
