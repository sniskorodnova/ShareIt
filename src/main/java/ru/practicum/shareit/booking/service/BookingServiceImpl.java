package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
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

/**
 * Класс, имплементирующий интерфейс для работы сервиса бронирований
 */
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

    /**
     * Метод для создания бронирования с валидациями
     */
    @Override
    public BookingDto create(Long userId, BookingCreateDto bookingCreate) throws ItemNotFoundException,
            UserNotFoundException, ValidationException {
        Booking booking = BookingMapper.toBookingCreate(bookingCreate);
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            if (itemRepository.findById(booking.getItem().getId()).isEmpty()) {
                throw new ItemNotFoundException("Item with id = "
                        + booking.getItem().getId() + " not found");
            } else {
                if (booking.getStart().isBefore(LocalDateTime.now())
                        || booking.getEnd().isBefore(LocalDateTime.now())
                        || booking.getStart().isAfter(booking.getEnd())) {
                    throw new ValidationException("Booking date is incorrect");
                } else {
                    if (!itemRepository.findById(booking.getItem().getId()).orElseThrow().getAvailable()) {
                        throw new ValidationException("Item with id = "
                                + booking.getItem().getId() + " isn't available");
                    } else {
                        if ((itemRepository.findById(booking.getItem().getId()).get().getOwnerId()).equals(userId)) {
                            throw new ItemNotFoundException("Item can't be booked by user " + userId);
                        } else {
                            booking.setStatus(Status.WAITING);
                            booking.setBooker(userRepository.findById(userId).get());
                            booking.setItem(itemRepository.findById(booking.getItem().getId()).orElseThrow());
                            return BookingMapper.toBookingDto(bookingRepository.save(booking));
                        }
                    }
                }
            }
        }
    }

    /**
     * Метод для изменения статуса бронирования с валидациями
     */
    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemNotBelongsToUserException, ValidationException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            if (bookingRepository.findById(bookingId).isEmpty()) {
                throw new BookingNotFoundException("Booking with id = " + bookingId + " not found");
            } else {
                Long itemId = bookingRepository.findById(bookingId).get().getItem().getId();
                if (!(itemRepository.findById(itemId).orElseThrow().getOwnerId().equals(userId))) {
                    throw new ItemNotBelongsToUserException("User " + userId + " can't update booking " + bookingId);
                } else {
                    if (bookingRepository.findById(bookingId).orElseThrow().getStatus().equals(Status.APPROVED)) {
                        throw new ValidationException("Booking with approve status can't be changed");
                    } else {
                        Booking foundBooking = bookingRepository.findById(bookingId).get();
                        if (approved.equals(true)) {
                            foundBooking.setStatus(Status.APPROVED);
                        } else {
                            foundBooking.setStatus(Status.REJECTED);
                        }
                        return BookingMapper.toBookingDto(bookingRepository.save(foundBooking));
                    }
                }
            }
        }
    }

    /**
     * Метод для получения информации о бронировании по его id
     */
    @Override
    public BookingDto getById(Long userId, Long bookingId) throws ItemNotBelongsToUserException, UserNotFoundException,
            BookingNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            if (bookingRepository.findById(bookingId).isEmpty()) {
                throw new BookingNotFoundException("There is no booking with id = " + bookingId);
            } else {
                Long bookerId = bookingRepository.findById(bookingId).get().getBooker().getId();
                Item itemFromBooking = itemRepository
                        .findById(bookingRepository.findById(bookingId).get().getItem().getId()).orElseThrow();
                if (!(bookerId.equals(userId)) && !((itemFromBooking.getOwnerId()).equals(userId))) {
                    throw new ItemNotBelongsToUserException("User " + userId + " can't view this booking");
                } else {
                    Booking booking = bookingRepository.findById(bookingId).get();
                    booking.setBooker(userRepository.findById(bookerId).orElseThrow());
                    booking.setItem(itemRepository.findById(booking.getItem().getId()).orElseThrow());
                    return BookingMapper.toBookingDto(booking);
                }
            }
        }
    }

    /**
     * Метод для получения всех бронирований пользователя в определенном статусе, запрошенном клиентом
     */
    @Override
    public List<BookingDto> getAllBookingsForRequester(Long userId, State state) throws UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            List<Booking> foundBookings = new ArrayList<>();
            switch (state) {
                case ALL:
                    foundBookings = bookingRepository.findByBooker_idOrderByStartDesc(userId);
                    break;
                case WAITING:
                    foundBookings = bookingRepository.findByBooker_idAndStatusOrderByStartDesc(userId, Status.WAITING);
                    break;
                case REJECTED:
                    foundBookings = bookingRepository.findByBooker_idAndStatusOrderByStartDesc(userId, Status.REJECTED);
                    break;
                case PAST:
                    foundBookings = bookingRepository.findByBooker_idAndEndBeforeOrderByStartDesc(userId,
                            LocalDateTime.now());
                    break;
                case FUTURE:
                    foundBookings = bookingRepository.findByBooker_idAndStartAfterOrderByStartDesc(userId,
                            LocalDateTime.now());
                    break;
                case CURRENT:
                    foundBookings = bookingRepository.findByBooker_idAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now());
                    break;
            }
            List<BookingDto> listBookingDto = new ArrayList<>();
            for (Booking booking : foundBookings) {
                listBookingDto.add(BookingMapper.toBookingDto(booking));
            }
            return listBookingDto;
        }
    }

    /**
     * Метод для получения списка бронирований для всех вещей пользователя, запрошенного с определенным статусом
     * от клиента
     */
    @Override
    public List<BookingDto> getAllBookingsForOwner(Long userId, State state) throws UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            List<Booking> foundBookings = new ArrayList<>();
            switch (state) {
                case ALL:
                    foundBookings = bookingRepository.findForOwnerAllStatus(userId);
                    break;
                case WAITING:
                    foundBookings = bookingRepository.findForOwnerStatus(userId, Status.WAITING);
                    break;
                case REJECTED:
                    foundBookings = bookingRepository.findForOwnerStatus(userId, Status.REJECTED);
                    break;
                case PAST:
                    foundBookings = bookingRepository.findForOwnerPast(userId, LocalDateTime.now());
                    break;
                case FUTURE:
                    foundBookings = bookingRepository.findForOwnerFuture(userId, LocalDateTime.now());
                    break;
                case CURRENT:
                    foundBookings = bookingRepository.findForOwnerCurrent(userId, LocalDateTime.now(),
                            LocalDateTime.now());
                    break;
            }
            List<BookingDto> listBookingDto = new ArrayList<>();
            for (Booking booking : foundBookings) {
                listBookingDto.add(BookingMapper.toBookingDto(booking));
            }
            return listBookingDto;
        }
    }
}
