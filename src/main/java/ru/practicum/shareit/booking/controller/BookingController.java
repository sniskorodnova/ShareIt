package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                             @Valid @RequestBody BookingCreateDto booking) throws ItemUnavailableException,
            ItemNotFoundException, UserNotFoundException, IncorrectTimeException {
        log.debug("Входящий запрос на создание запроса на бронирование: " + booking.toString());
        return BookingMapper.toBookingDto(bookingService.create(userId,
                BookingMapper.toBookingCreate(booking)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                          @PathVariable Long bookingId, @RequestParam Boolean approved) throws NoHeaderException,
            UserNotFoundException, BookingNotFoundException, ItemNotBelongsToUserException {
        log.debug("Входящий запрос на редактирование статуса запроса на бронирование");
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return BookingMapper.toBookingDto(bookingService.update(userId, bookingId, approved));
        }
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                             @PathVariable Long bookingId) throws NoHeaderException,
            UserNotFoundException, BookingNotFoundException, ItemNotBelongsToUserException {
        log.debug("Входящий запрос на получение информации по бронированию с id = {}", bookingId);
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            return BookingMapper.toBookingDto(bookingService.getById(userId, bookingId));
        }
    }

    @GetMapping()
    public List<BookingDto> getAllBookingsForRequester(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                                      Long userId, @RequestParam(defaultValue = "ALL") State state)
            throws NoHeaderException, UserNotFoundException {
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            log.debug("Входящий запрос на получение информации по всем бронированиям для пользователю {}", userId);
            List<BookingDto> listBookingDto = new ArrayList<>();
            for (Booking booking : bookingService.getAllBookingsForRequester(userId, state)) {
                listBookingDto.add(BookingMapper.toBookingDto(booking));
            }
            return listBookingDto;
        }
    }

    @GetMapping("/bookings/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                                       Long userId, @RequestParam State state)
            throws NoHeaderException {
        if (userId == null) {
            throw new NoHeaderException("No header in the request");
        } else {
            log.debug("Входящий запрос на получение информации по всем бронированиям для владельца {}", userId);
            List<BookingDto> listBookingDto = new ArrayList<>();
            for (Booking booking : bookingService.getAllBookingsForOwner(userId, state)) {
                listBookingDto.add(BookingMapper.toBookingDto(booking));
            }
            return listBookingDto;
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemNotAvailable(final ItemUnavailableException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFound(final ItemNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectTime(final IncorrectTimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingNotFound(final BookingNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemNotBelongsToUser(final ItemNotBelongsToUserException e) {
        return new ErrorResponse(e.getMessage());
    }
}
