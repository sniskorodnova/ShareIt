package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Booking create(Long userId, Booking booking) throws ItemUnavailableException,
            ItemNotFoundException, UserNotFoundException, IncorrectTimeException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            if (itemRepository.findById(booking.getItem().getId()).isEmpty()) {
                throw new ItemNotFoundException("Item with id = "
                        + booking.getItem() + " not found");
            } else {
                if (booking.getStart().isBefore(LocalDateTime.now())
                        || booking.getEnd().isBefore(LocalDateTime.now())) {
                    throw new IncorrectTimeException("Start date can't be later than end date");
                } else {
                    if (!itemRepository.findById(booking.getItem().getId()).get().getAvailable()) {
                        throw new ItemUnavailableException("Item with id = "
                                + booking.getItem() + " isn't available");
                    } else {
                        booking.setStatus(Status.WAITING);
                        booking.setBooker(userRepository.findById(userId).get());
                        booking.setItem(itemRepository.findById(booking.getItem().getId()).orElseThrow());
                        return bookingRepository.save(booking);
                    }
                }
            }
        }
    }

    @Override
    public Booking update(Long userId, Long bookingId, Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemNotBelongsToUserException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            if (bookingRepository.findById(bookingId).isEmpty()) {
                throw new BookingNotFoundException("Booking with id = " + bookingId + " not found");
            } else {
                if (!(bookingRepository.findById(bookingId).get().getBooker().getId()).equals(userId)) {
                    throw new ItemNotBelongsToUserException("User " + userId + " can't update booking " + bookingId);
                } else {
                    Booking foundBooking = bookingRepository.findById(bookingId).get();
                    if (approved.equals(true)) {
                        foundBooking.setStatus(Status.APPROVED);
                    } else {
                        foundBooking.setStatus(Status.REJECTED);
                    }
                    return bookingRepository.save(foundBooking);
                }
            }
        }
    }

    @Override
    public Booking getById(Long userId, Long bookingId) throws ItemNotBelongsToUserException, UserNotFoundException,
            BookingNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            Item itemFromBooking = itemRepository
                    .findById(bookingRepository.findById(bookingId).get().getItem().getId()).orElseThrow();
            Long bookerId = bookingRepository.findById(bookingId).get().getBooker().getId();
            if (!(bookerId.equals(userId)) && !((itemFromBooking.getOwnerId()).equals(userId))) {
                throw new ItemNotBelongsToUserException("User " + userId + " can't view this booking");
            } else {
                if (bookingRepository.findById(bookingId).isEmpty()) {
                    throw new BookingNotFoundException("There is no booking with id = " + bookingId);
                } else {
                    Booking booking = bookingRepository.findById(bookingId).get();
                    booking.setBooker(userRepository.findById(bookerId).orElseThrow());
                    booking.setItem(itemRepository.findById(booking.getItem().getId()).orElseThrow());
                    return booking;
                }
            }
        }
    }

    @Override
    public List<Booking> getAllBookingsForRequester(Long userId, State state) throws UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            List<Booking> foundBookings = new ArrayList<>();
            switch (state) {
                case ALL:
                    foundBookings = bookingRepository.findByBooker(userId);
                    break;
                case WAITING:
                    foundBookings = bookingRepository.findByBookerAndStatus(userId, Status.WAITING);
                    break;
                case REJECTED:
                    foundBookings = bookingRepository.findByBookerAndStatus(userId, Status.REJECTED);
                    break;
                case PAST:
                    foundBookings = bookingRepository.findByBookerAndEndBefore(userId, LocalDateTime.now());
                    break;
                case FUTURE:
                    foundBookings = bookingRepository.findByBookerAndStartAfter(userId, LocalDateTime.now());
                    break;
                case CURRENT:
                    foundBookings = bookingRepository.findByBookerAndStartBeforeAndEndBefore(userId,
                                    LocalDateTime.now(), LocalDateTime.now());
                    break;
            }
            return foundBookings;
        }
    }

    @Override
    public List<Booking> getAllBookingsForOwner(Long userId, State state) {
        return null;
    }
}
