package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exception.NoHeaderException;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public Object create(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                         @RequestBody @Valid BookingCreateDto booking) throws NoHeaderException {
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            log.debug("Входящий запрос на создание запроса на бронирование: " + booking.toString());
            return bookingClient.create(userId, booking);
        }
    }

    @PatchMapping("/{bookingId}")
    public Object update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                         @PathVariable Long bookingId, @RequestParam Boolean approved) throws NoHeaderException {
        log.debug("Входящий запрос на редактирование статуса запроса на бронирование");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingClient.update(userId, bookingId, approved);
        }
    }

    @GetMapping("/{bookingId}")
    public Object getById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @PathVariable Long bookingId) throws NoHeaderException {
        log.debug("Входящий запрос на получение информации по бронированию с id = {}", bookingId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingClient.getById(userId, bookingId);
        }
    }

    @GetMapping
    public Object getAllBookingsForRequester(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException, UnsupportedStatusException {
        try {
            State stateToConvert = State.valueOf(state);
            if (userId == null) {
                throw new NoHeaderException("No header in the request");
            } else {
                log.debug("Входящий запрос на получение информации по всем бронированиям для пользователю {}", userId);
                return bookingClient.getAllBookingsForRequester(userId, stateToConvert, from, size);
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }

    @GetMapping("/owner")
    public Object getAllBookingsForOwner(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException, UnsupportedStatusException {
        try {
            State stateToConvert = State.valueOf(state);
            if (userId == null) {
                throw new NoHeaderException("No header in the request");
            } else {
                log.debug("Входящий запрос на получение информации по всем бронированиям для владельца {}", userId);
                return bookingClient.getAllBookingsForOwner(userId, stateToConvert, from, size);
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }
}