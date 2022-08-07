package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.*;

import java.util.List;

/**
 * Интерфейс, описывающий логику для работы сервиса бронирований
 */
public interface BookingService {
    BookingDto create(Long userId, BookingCreateDto booking) throws ItemNotFoundException, UserNotFoundException,
            ValidationException;

    BookingDto update(Long userId, Long bookingId, Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemNotBelongsToUserException, ValidationException;

    BookingDto getById(Long userId, Long bookingId) throws ItemNotBelongsToUserException, UserNotFoundException,
            BookingNotFoundException;

    List<BookingDto> getAllBookingsForRequesterWithPagination(Long userId, State state, Integer from, Integer size)
            throws UserNotFoundException;

    List<BookingDto> getAllBookingsForOwnerWithPagination(Long userId, State state, Integer from, Integer size)
            throws UserNotFoundException;
}
