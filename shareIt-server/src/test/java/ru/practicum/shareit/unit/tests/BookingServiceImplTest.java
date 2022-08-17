package ru.practicum.shareit.unit.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void testCreateBookingSuccess() throws UserNotFoundException, ValidationException,
            ItemNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(start, end, 1L);
        Booking booking = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        BookingDto bookingDto = new BookingDto(1L, start, end, item, userBooker, Status.WAITING);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        BookingDto bookingDtoResult = bookingService.create(1L, bookingCreateDto);
        assertThat(bookingDtoResult.getId(), equalTo(bookingDto.getId()));
        assertThat(bookingDtoResult.getStart(), equalTo(bookingDto.getStart()));
        assertThat(bookingDtoResult.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(bookingDtoResult.getStatus(), equalTo(bookingDto.getStatus()));
    }

    @Test
    void testCreateBookingUserNotFoundException() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(start, end, 1L);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);

        assertThrows(UserNotFoundException.class, () ->
                bookingService.create(1L, bookingCreateDto));
    }

    @Test
    void testCreateBookingItemNotFound() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(start, end, 1L);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        assertThrows(ItemNotFoundException.class, () ->
                bookingService.create(1L, bookingCreateDto));
    }

    @Test
    void testCreateBookingDateIsIncorrect() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().minusDays(2);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(start, end, 1L);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        assertThrows(ValidationException.class, () ->
                bookingService.create(1L, bookingCreateDto));
    }

    @Test
    void testCreateBookingItemNotAvailable() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(start, end, 1L);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", false, 2L,
                null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        assertThrows(ValidationException.class, () ->
                bookingService.create(1L, bookingCreateDto));
    }

    @Test
    void testCreateBookingItemCantBeBookedByUser() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(start, end, 1L);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 1L,
                null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        assertThrows(ItemNotFoundException.class, () ->
                bookingService.create(1L, bookingCreateDto));
    }

    @Test
    void testUpdateBookingToApprovedSuccess() throws UserNotFoundException, BookingNotFoundException,
            ValidationException, ItemNotBelongsToUserException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 1L,
                null);
        Booking booking = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Booking bookingDto = new Booking(1L, start, end, item, userBooker, Status.APPROVED);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(bookingDto);

        BookingDto bookingDtoResult = bookingService.update(1L, 1L, true);
        assertThat(bookingDtoResult.getId(), equalTo(bookingDto.getId()));
        assertThat(bookingDtoResult.getStart(), equalTo(bookingDto.getStart()));
        assertThat(bookingDtoResult.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(bookingDtoResult.getStatus(), equalTo(bookingDto.getStatus()));
    }

    @Test
    void testUpdateBookingToRejectedSuccess() throws UserNotFoundException, BookingNotFoundException,
            ValidationException, ItemNotBelongsToUserException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 1L,
                null);
        Booking booking = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Booking bookingDto = new Booking(1L, start, end, item, userBooker, Status.REJECTED);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(bookingDto);

        BookingDto bookingDtoResult = bookingService.update(1L, 1L, false);
        assertThat(bookingDtoResult.getId(), equalTo(bookingDto.getId()));
        assertThat(bookingDtoResult.getStart(), equalTo(bookingDto.getStart()));
        assertThat(bookingDtoResult.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(bookingDtoResult.getStatus(), equalTo(bookingDto.getStatus()));
    }

    @Test
    void testUpdateBookingUserNotFoundException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        assertThrows(UserNotFoundException.class, () ->
                bookingService.update(1L, 1L, true));
    }

    @Test
    void testUpdateBookingBookingNotFoundException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        assertThrows(BookingNotFoundException.class, () ->
                bookingService.update(1L, 2L, true));
    }

    @Test
    void testUpdateBookingItemNotBelongsToUserException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking booking = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        assertThrows(ItemNotBelongsToUserException.class, () ->
                bookingService.update(1L, 1L, true));
    }

    @Test
    void testUpdateBookingStatusCantBeChangedException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 1L,
                null);
        Booking booking = new Booking(1L, start, end, item, userBooker, Status.APPROVED);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        assertThrows(ValidationException.class, () ->
                bookingService.update(1L, 1L, true));
    }

    @Test
    void testGetBookingByIdSuccess() throws UserNotFoundException, BookingNotFoundException,
            ItemNotBelongsToUserException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking booking = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        BookingDto bookingDto = new BookingDto(1L, start, end, item, userBooker, Status.WAITING);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDto bookingDtoResult = bookingService.getById(1L, 1L);
        assertThat(bookingDtoResult.getId(), equalTo(bookingDto.getId()));
        assertThat(bookingDtoResult.getStart(), equalTo(bookingDto.getStart()));
        assertThat(bookingDtoResult.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(bookingDtoResult.getStatus(), equalTo(bookingDto.getStatus()));
    }

    @Test
    void testGetBookingByIdUserNotFoundException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        assertThrows(UserNotFoundException.class, () ->
                bookingService.getById(1L, 1L));
    }

    @Test
    void testGetBookingByIdBookingNotFoundException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        assertThrows(BookingNotFoundException.class, () ->
                bookingService.getById(1L, 1L));
    }

    @Test
    void testGetBookingByIdItemNotBelongsToUserException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 1L,
                null);
        Booking booking = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        assertThrows(ItemNotBelongsToUserException.class, () ->
                bookingService.getById(2L, 1L));
    }

    @Test
    void testGetBookingsForRequesterWithPaginationAllStatuses() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Booking bookingSecond = new Booking(2L, start.minusDays(2), end, item, userBooker, Status.APPROVED);
        Booking bookingThird = new Booking(3L, start.minusDays(1), end, item, userBooker, Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingThird);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findByBooker_idOrderByStartDesc(anyLong(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForRequesterWithPagination(1L, State.ALL,
                0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingThird.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingThird.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingThird.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingThird.getStatus()));
        assertThat(bookingDtoResult.get(2).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(2).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(2).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(2).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForRequesterWithPaginationWaitingStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Booking bookingSecond = new Booking(2L, start.minusDays(2), end, item, userBooker, Status.WAITING);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findByBooker_idAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForRequesterWithPagination(1L,
                State.WAITING, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForRequesterWithPaginationRejectedStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker, Status.REJECTED);
        Booking bookingSecond = new Booking(2L, start.minusDays(2), end, item, userBooker, Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findByBooker_idAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForRequesterWithPagination(1L,
                State.REJECTED, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForRequesterWithPaginationPastStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start.minusDays(5), end.minusDays(6), item, userBooker,
                Status.APPROVED);
        Booking bookingSecond = new Booking(2L, start.minusDays(10), end.minusDays(7), item, userBooker,
                Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findByBooker_idAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForRequesterWithPagination(1L,
                State.PAST, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForRequesterWithPaginationFutureStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker,
                Status.APPROVED);
        Booking bookingSecond = new Booking(2L, start.plusDays(2), end.plusDays(2), item, userBooker,
                Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findByBooker_idAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForRequesterWithPagination(1L,
                State.FUTURE, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForRequesterWithPaginationCurrentStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start.minusDays(3), end, item, userBooker,
                Status.APPROVED);
        Booking bookingSecond = new Booking(2L, start.minusDays(4), end.plusDays(1), item, userBooker,
                Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findByBooker_idAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(),
                        any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForRequesterWithPagination(1L,
                State.CURRENT, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForRequesterWithPaginationUserNotFoundException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        assertThrows(UserNotFoundException.class, () ->
                bookingService.getAllBookingsForRequesterWithPagination(1L, State.ALL, 0, 5));
    }

    @Test
    void testGetBookingsForOwnerWithPaginationUserNotFoundException() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        assertThrows(UserNotFoundException.class, () ->
                bookingService.getAllBookingsForOwnerWithPagination(1L, State.ALL, 0, 5));
    }

    @Test
    void testGetBookingsForOwnerWithPaginationAllStatuses() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Booking bookingSecond = new Booking(2L, start.minusDays(2), end, item, userBooker, Status.APPROVED);
        Booking bookingThird = new Booking(3L, start.minusDays(1), end, item, userBooker, Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingThird);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findForOwnerAllStatus(anyLong(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForOwnerWithPagination(1L, State.ALL,
                0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingThird.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingThird.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingThird.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingThird.getStatus()));
        assertThat(bookingDtoResult.get(2).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(2).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(2).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(2).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForOwnerWithPaginationWaitingStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker, Status.WAITING);
        Booking bookingSecond = new Booking(2L, start.minusDays(2), end, item, userBooker, Status.WAITING);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findForOwnerStatus(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForOwnerWithPagination(1L,
                State.WAITING, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForOwnerWithPaginationRejectedStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker, Status.REJECTED);
        Booking bookingSecond = new Booking(2L, start.minusDays(2), end, item, userBooker, Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findForOwnerStatus(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForOwnerWithPagination(1L,
                State.REJECTED, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForOwnerWithPaginationPastStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start.minusDays(5), end.minusDays(6), item, userBooker,
                Status.APPROVED);
        Booking bookingSecond = new Booking(2L, start.minusDays(10), end.minusDays(7), item, userBooker,
                Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findForOwnerPast(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForOwnerWithPagination(1L,
                State.PAST, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForOwnerWithPaginationFutureStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start, end, item, userBooker,
                Status.APPROVED);
        Booking bookingSecond = new Booking(2L, start.plusDays(2), end.plusDays(2), item, userBooker,
                Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findForOwnerFuture(anyLong(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForOwnerWithPagination(1L,
                State.FUTURE, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }

    @Test
    void testGetBookingsForOwnerWithPaginationCurrentStatus() throws UserNotFoundException {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 2L,
                null);
        Booking bookingFirst = new Booking(1L, start.minusDays(3), end, item, userBooker,
                Status.APPROVED);
        Booking bookingSecond = new Booking(2L, start.minusDays(4), end.plusDays(1), item, userBooker,
                Status.REJECTED);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(bookingFirst);
        bookingList.add(bookingSecond);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(bookingRepository.findForOwnerCurrent(anyLong(), any(), any(), any()))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoResult = bookingService.getAllBookingsForOwnerWithPagination(1L,
                State.CURRENT, 0, 5);
        assertThat(bookingDtoResult.get(0).getId(), equalTo(bookingFirst.getId()));
        assertThat(bookingDtoResult.get(0).getStart(), equalTo(bookingFirst.getStart()));
        assertThat(bookingDtoResult.get(0).getEnd(), equalTo(bookingFirst.getEnd()));
        assertThat(bookingDtoResult.get(0).getStatus(), equalTo(bookingFirst.getStatus()));
        assertThat(bookingDtoResult.get(1).getId(), equalTo(bookingSecond.getId()));
        assertThat(bookingDtoResult.get(1).getStart(), equalTo(bookingSecond.getStart()));
        assertThat(bookingDtoResult.get(1).getEnd(), equalTo(bookingSecond.getEnd()));
        assertThat(bookingDtoResult.get(1).getStatus(), equalTo(bookingSecond.getStatus()));
    }
}
