package ru.practicum.shareit.jpa.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findBookingListForUserOrderByStartDesc() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository.findByBooker_idOrderByStartDesc(2L,
                PageRequest.of(0, 5));
        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingFirst);
        listToCompare.add(bookingSecond);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForUserOrderByStartDescWithEndDateBefore() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository.findByBooker_idAndEndBeforeOrderByStartDesc(2L,
                endSecond.plusHours(10), PageRequest.of(0, 5));
        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingSecond);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForUserOrderByStartDescWithStartDateAfter() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository.findByBooker_idAndStartAfterOrderByStartDesc(2L,
                startSecond.plusHours(5), PageRequest.of(0, 5));
        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingFirst);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForUserOrderByStartDescWithStatusWaiting() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository.findByBooker_idAndStatusOrderByStartDesc(2L,
                Status.WAITING, PageRequest.of(0, 5));
        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingFirst);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForUserOrderByStartDescWithStartBeforeAndEndAfter() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository
                .findByBooker_idAndStartBeforeAndEndAfterOrderByStartDesc(2L, startSecond.plusHours(7),
                        endSecond.minusHours(5), PageRequest.of(0, 5));
        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingSecond);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForOwnerAllStatuses() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);
        Booking bookingThird = new Booking(3L, startSecond, endSecond, item, userBooker, Status.REJECTED);
        bookingRepository.save(bookingThird);

        List<Booking> foundBookings = bookingRepository.findForOwnerAllStatus(userOwner.getId(),
                PageRequest.of(0, 5));

        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingFirst);
        listToCompare.add(bookingSecond);
        listToCompare.add(bookingThird);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForOwnerWaitingStatus() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);
        Booking bookingThird = new Booking(3L, startSecond, endSecond, item, userBooker, Status.REJECTED);
        bookingRepository.save(bookingThird);

        List<Booking> foundBookings = bookingRepository.findForOwnerStatus(userOwner.getId(), Status.WAITING,
                PageRequest.of(0, 5));

        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingFirst);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForOwnerApprovedStatus() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);
        Booking bookingThird = new Booking(3L, startSecond, endSecond, item, userBooker, Status.REJECTED);
        bookingRepository.save(bookingThird);

        List<Booking> foundBookings = bookingRepository.findForOwnerStatus(userOwner.getId(), Status.APPROVED,
                PageRequest.of(0, 5));

        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingSecond);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForOwnerPast() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository.findForOwnerPast(userOwner.getId(), endSecond.plusHours(7),
                PageRequest.of(0, 5));

        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingSecond);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForOwnerFuture() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository.findForOwnerFuture(userOwner.getId(), startSecond.plusHours(7),
                PageRequest.of(0, 5));

        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingFirst);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }

    @Test
    public void findBookingListForOwnerCurrent() {
        User userOwner = new User(1L, "User owner", "qwe@gmail.com");
        userRepository.save(userOwner);
        User userBooker = new User(2L, "User booker", "azq@gmail.com");
        userRepository.save(userBooker);

        LocalDateTime startFirst = LocalDateTime.now().plusDays(2L);
        LocalDateTime endFirst = LocalDateTime.now().plusDays(4L);
        LocalDateTime startSecond = LocalDateTime.now().plusDays(1L);
        LocalDateTime endSecond = LocalDateTime.now().plusDays(3L);

        Item item = new Item(1L, "Item name", "Item description", true,
                1L, null);
        itemRepository.save(item);

        Booking bookingFirst = new Booking(1L, startFirst, endFirst, item, userBooker, Status.WAITING);
        bookingRepository.save(bookingFirst);
        Booking bookingSecond = new Booking(2L, startSecond, endSecond, item, userBooker, Status.APPROVED);
        bookingRepository.save(bookingSecond);

        List<Booking> foundBookings = bookingRepository.findForOwnerCurrent(userOwner.getId(), startFirst.plusDays(2),
                endSecond.minusDays(1), PageRequest.of(0, 5));

        List<Booking> listToCompare = new ArrayList<>();
        listToCompare.add(bookingFirst);
        listToCompare.add(bookingSecond);
        assertThat(foundBookings, is(equalTo(listToCompare)));
    }
}
