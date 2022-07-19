package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.*;

import java.util.List;

/**
 * Интерфейс, описывающий логику для работы сервиса бронирований
 */
public interface BookingService {
    Booking create(Long userId, Booking booking) throws ItemNotFoundException, UserNotFoundException,
            ValidationException;

    Booking update(Long userId, Long bookingId, Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemNotBelongsToUserException, ValidationException;

    Booking getById(Long userId, Long bookingId) throws ItemNotBelongsToUserException, UserNotFoundException,
            BookingNotFoundException;

    List<Booking> getAllBookingsForRequester(Long userId, State state) throws UserNotFoundException;

    List<Booking> getAllBookingsForOwner(Long userId, State state) throws UserNotFoundException;
}
