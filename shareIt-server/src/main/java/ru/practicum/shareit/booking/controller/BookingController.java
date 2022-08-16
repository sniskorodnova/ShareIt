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
    public static final String USER_HEADER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    /**
     * Метод для создания бронирования
     */
    @PostMapping
    public BookingDto create(@RequestHeader(value = USER_HEADER_ID) Long userId,
                             @RequestBody BookingCreateDto booking) throws ItemNotFoundException,
            UserNotFoundException, ValidationException {
        return bookingService.create(userId, booking);
    }

    /**
     * Метод для изменения статуса бронирования. Изменять статус может только владелец вещи
     */
    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(value = USER_HEADER_ID) Long userId,
                             @PathVariable Long bookingId, @RequestParam Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemNotBelongsToUserException, ValidationException {
        return bookingService.update(userId, bookingId, approved);
    }

    /**
     * Метод для получения информации о бронировании
     */
    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(value = USER_HEADER_ID) Long userId,
                              @PathVariable Long bookingId) throws UserNotFoundException, BookingNotFoundException,
            ItemNotBelongsToUserException {
        return bookingService.getById(userId, bookingId);
    }

    /**
     * Метод для получения всех бронирований пользователя в определенном статусе, запрошенном клиентом
     */
    @GetMapping()
    public List<BookingDto> getAllBookingsForRequester(@RequestHeader(value = USER_HEADER_ID)
                                                       Long userId, @RequestParam State state,
                                                       @RequestParam Integer from, @RequestParam Integer size)
            throws UserNotFoundException {
        return bookingService.getAllBookingsForRequesterWithPagination(userId, state, from, size);
    }

    /**
     * Метод для получения списка бронирований для всех вещей пользователя, запрошенного с определенным статусом
     * от клиента
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(value = USER_HEADER_ID) Long userId,
                                                   @RequestParam State state, @RequestParam Integer from,
                                                   @RequestParam Integer size) throws UserNotFoundException {
        return bookingService.getAllBookingsForOwnerWithPagination(userId, state, from, size);
    }
}
