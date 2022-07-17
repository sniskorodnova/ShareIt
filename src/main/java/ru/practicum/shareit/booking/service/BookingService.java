package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.*;

import java.util.List;

public interface BookingService {
    Booking create(Long userId, Booking booking) throws ItemUnavailableException, ItemNotFoundException,
            UserNotFoundException, IncorrectTimeException;

    Booking update(Long userId, Long bookingId, Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemNotBelongsToUserException;

    Booking getById(Long userId, Long bookingId) throws ItemNotBelongsToUserException, UserNotFoundException,
            BookingNotFoundException;

    List<Booking> getAllBookingsForRequester(Long userId, State state) throws UserNotFoundException;

    List<Booking> getAllBookingsForOwner(Long userId, State state);
}
