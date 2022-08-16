package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.NoHeaderException;

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
    public Object create(@RequestHeader(value = BaseClient.USER_ID_HEADER, required = false) Long userId,
                         @RequestBody @Valid BookingCreateDto booking) throws NoHeaderException {
        log.info("Входящий запрос на создание запроса на бронирование: " + booking.toString());
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingClient.create(userId, booking);
        }
    }

    @PatchMapping("/{bookingId}")
    public Object update(@RequestHeader(value = BaseClient.USER_ID_HEADER, required = false) Long userId,
                         @PathVariable Long bookingId, @RequestParam Boolean approved) throws NoHeaderException {
        log.info("Входящий запрос на редактирование статуса запроса на бронирование");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingClient.update(userId, bookingId, approved);
        }
    }

    @GetMapping("/{bookingId}")
    public Object getById(@RequestHeader(value = BaseClient.USER_ID_HEADER, required = false) Long userId,
                          @PathVariable Long bookingId) throws NoHeaderException {
        log.info("Входящий запрос на получение информации по бронированию с id = {}", bookingId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingClient.getById(userId, bookingId);
        }
    }

    @GetMapping
    public Object getAllBookingsForRequester(@RequestHeader(value = BaseClient.USER_ID_HEADER, required = false)
                                             Long userId, @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException {
        log.info("Входящий запрос на получение информации по всем бронированиям для пользователю {}", userId);
        State stateToConvert = State.from(state).orElseThrow(()
                -> new IllegalArgumentException("Unknown state: " + state));
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingClient.getAllBookingsForRequester(userId, stateToConvert, from, size);
        }
    }

    @GetMapping("/owner")
    public Object getAllBookingsForOwner(@RequestHeader(value = BaseClient.USER_ID_HEADER, required = false) Long userId,
                                         @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size)
            throws NoHeaderException {
        log.info("Входящий запрос на получение информации по всем бронированиям для владельца {}", userId);
        State stateToConvert = State.from(state).orElseThrow(()
                -> new IllegalArgumentException("Unknown state: " + state));
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return bookingClient.getAllBookingsForOwner(userId, stateToConvert, from, size);
        }
    }
}