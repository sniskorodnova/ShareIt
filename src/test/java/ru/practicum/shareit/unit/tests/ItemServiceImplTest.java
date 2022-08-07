package ru.practicum.shareit.unit.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @Test
    void testCreateItemSuccess() throws UserNotFoundException, ValidationException {
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true, 1L,
                1L);
        ItemCreateDto itemCreateDto = new ItemCreateDto("Item name", "Item description", true,
                1L);
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        ItemDto itemDtoResult = itemService.create(1L, itemCreateDto);
        assertThat(itemDtoResult.getId(), equalTo(item.getId()));
        assertThat(itemDtoResult.getName(), equalTo(item.getName()));
        assertThat(itemDtoResult.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDtoResult.getAvailable(), equalTo(item.getAvailable()));
        assertThat(itemDtoResult.getRequestId(), equalTo(item.getRequestId()));
    }

    @Test
    void testCreateItemUserNotFoundException() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Item name", "Item description", true,
                1L);
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        assertThrows(UserNotFoundException.class, () ->
                itemService.create(1L, itemCreateDto));
    }

    @Test
    void testUpdateItemSuccess() throws ValidationException, AuthFailedException {
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name update", "Item description update", true,
                1L, 1L);
        ItemCreateDto itemUpdatedCreateDto = new ItemCreateDto("Item name update",
                "Item description update", true, 1L);
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        ItemDto itemDtoResult = itemService.update(1L, 1L, itemUpdatedCreateDto);
        assertThat(itemDtoResult.getId(), equalTo(item.getId()));
        assertThat(itemDtoResult.getName(), equalTo(item.getName()));
        assertThat(itemDtoResult.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDtoResult.getAvailable(), equalTo(item.getAvailable()));
        assertThat(itemDtoResult.getRequestId(), equalTo(item.getRequestId()));
    }

    @Test
    void testUpdateItemDoesntBelongToUser() throws ValidationException, AuthFailedException {
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name update", "Item description update", true,
                1L, 1L);
        Item itemOther = new Item(2L, "Item name other", "Item description other", true,
                2L, 2L);
        ItemCreateDto itemUpdatedCreateDto = new ItemCreateDto("Item name update",
                "Item description update", true, 1L);
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(itemOther));

        assertThrows(AuthFailedException.class, () ->
                itemService.update(1L, 1L, itemUpdatedCreateDto));
    }

    @Test
    void testGetItemByIdSuccess() throws UserNotFoundException, ItemNotFoundException {
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item item = new Item(1L, "Item name", "Item description", true,
                1L, 2L);
        ItemDtoWithComment itemDtoWithComment = new ItemDtoWithComment(1L, "Item name",
                "Item description", true, 2L, null, null,
                null);
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        ItemDtoWithComment itemDtoResult = itemService.getById(1L, 1L);
        assertThat(itemDtoResult.getId(), equalTo(itemDtoWithComment.getId()));
        assertThat(itemDtoResult.getName(), equalTo(itemDtoWithComment.getName()));
        assertThat(itemDtoResult.getDescription(), equalTo(itemDtoWithComment.getDescription()));
        assertThat(itemDtoResult.getAvailable(), equalTo(itemDtoWithComment.getAvailable()));
        assertThat(itemDtoResult.getRequestId(), equalTo(itemDtoWithComment.getRequestId()));
    }

    @Test
    void testGetItemByIdUserNotFoundException() {
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        assertThrows(UserNotFoundException.class, () ->
                itemService.getById(1L, 1L));
    }

    @Test
    void testGetItemByIdItemNotFoundException() {
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        assertThrows(ItemNotFoundException.class, () ->
                itemService.getById(1L, 1L));
    }

    @Test
    void testGetAllItemsWithPaginationNoUserException() {
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        assertThrows(UserNotFoundException.class, () ->
                itemService.getAllWithPagination(1L, 0, 5));
    }

    @Test
    void testSearchItemByText() throws UserNotFoundException, ItemNotFoundException {
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item itemDtoFirst = new Item(1L, "Item name first", "Item description first",
                true, 1L, 2L);
        Item itemDtoSecond = new Item(1L, "Item name second", "Item description second",
                false, 2L, 2L);
        List<Item> listItems = new ArrayList<>();
        listItems.add(itemDtoFirst);
        listItems.add(itemDtoSecond);
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.search(anyString(), any()))
                .thenReturn(listItems);

        List<ItemDto> itemDtoResult = itemService.searchByTextWithPagination(1L, "Item",
                0, 5);
        assertThat(itemDtoResult.get(0).getId(), equalTo(itemDtoFirst.getId()));
        assertThat(itemDtoResult.get(0).getName(), equalTo(itemDtoFirst.getName()));
        assertThat(itemDtoResult.get(0).getDescription(), equalTo(itemDtoFirst.getDescription()));
        assertThat(itemDtoResult.get(0).getAvailable(), equalTo(itemDtoFirst.getAvailable()));
        assertThat(itemDtoResult.get(0).getRequestId(), equalTo(itemDtoFirst.getRequestId()));
        assertThat(itemDtoResult.get(1).getId(), equalTo(itemDtoSecond.getId()));
        assertThat(itemDtoResult.get(1).getName(), equalTo(itemDtoSecond.getName()));
        assertThat(itemDtoResult.get(1).getDescription(), equalTo(itemDtoSecond.getDescription()));
        assertThat(itemDtoResult.get(1).getAvailable(), equalTo(itemDtoSecond.getAvailable()));
        assertThat(itemDtoResult.get(1).getRequestId(), equalTo(itemDtoSecond.getRequestId()));
    }

    @Test
    void testSearchItemsNoUserException() {
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        assertThrows(UserNotFoundException.class, () ->
                itemService.searchByTextWithPagination(1L, "Item", 0, 5));
    }

    @Test
    void testCreateCommentSuccess() throws UserNotFoundException, ValidationException {
        User userBooker = new User(1L, "Booker name", "booker@gmail.com");
        Item itemDto = new Item(1L, "Item name", "Item description", true, 1L,
                2L);
        CommentCreateDto commentCreateDto = new CommentCreateDto("Comment text");
        LocalDate created = LocalDate.now().plusDays(2);
        CommentDto commentDto = new CommentDto(1L, "Comment text", itemDto, "Booker name", created);
        Comment comment = new Comment(1L, "Comment text", itemDto, userBooker, created);
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        Booking booking = new Booking(1L, start, end, itemDto, userBooker, Status.WAITING);
        List<Booking> listBooking = new ArrayList<>();
        listBooking.add(booking);
        ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userBooker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemDto));
        Mockito
                .when(bookingRepository.findForItem(anyLong(), anyLong()))
                .thenReturn(listBooking);
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto commentResult = itemService.createComment(1L, 1L, commentCreateDto);
        assertThat(commentResult.getId(), equalTo(commentDto.getId()));
        assertThat(commentResult.getText(), equalTo(commentDto.getText()));
        assertThat(commentResult.getCreated(), equalTo((commentDto.getCreated())));
        assertThat(commentResult.getAuthorName(), equalTo((commentDto.getAuthorName())));
    }
}
