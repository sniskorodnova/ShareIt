package ru.practicum.shareit.unit.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
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
public class ItemRequestServiceImplTest {
    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;

    @Test
    void testCreateItemSuccess() throws UserNotFoundException {
        User user = new User(1L, "User name", "user@gmail.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto("Item request description");
        ItemRequest itemRequest = new ItemRequest(1L, "Item request description", 2L, created);
        ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository,
                itemRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        ItemRequestDto itemRequestResult = itemRequestService.create(1L, itemRequestCreateDto);
        assertThat(itemRequestResult.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestResult.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestResult.getCreated(), equalTo(itemRequest.getCreated()));
    }

    @Test
    void testCreateItemUserNotFoundException() {
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto("Item request description");
        ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository,
                itemRepository);
        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.create(1L, itemRequestCreateDto));
    }

    @Test
    void testGetItemByIdSuccess() throws UserNotFoundException, ItemRequestNotFoundException {
        User user = new User(1L, "User name", "user@gmail.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest = new ItemRequest(1L, "Item request description", 2L, created);
        Item itemFirst = new Item(1L, "Item first name", "Item first description",
                false, 2L, null);
        Item itemSecond = new Item(2L, "Item second name", "Item second description",
                false, 2L, null);
        List<Item> itemList = new ArrayList<>();
        itemList.add(itemFirst);
        itemList.add(itemSecond);
        ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository,
                itemRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(itemRepository.getItemsForRequest(anyLong()))
                .thenReturn(itemList);

        ItemRequestDtoWithItems itemRequestResult = itemRequestService.getById(1L, 1L);
        assertThat(itemRequestResult.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestResult.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestResult.getCreated(), equalTo(itemRequest.getCreated()));
    }

    @Test
    void testGetItemByIdItemRequestNotFoundException() {
        User user = new User(1L, "User name", "user@gmail.com");
        Item itemFirst = new Item(1L, "Item first name", "Item first description",
                false, 2L, null);
        Item itemSecond = new Item(2L, "Item second name", "Item second description",
                false, 2L, null);
        List<Item> itemList = new ArrayList<>();
        itemList.add(itemFirst);
        itemList.add(itemSecond);
        ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository,
                itemRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        assertThrows(ItemRequestNotFoundException.class, () ->
                itemRequestService.getById(1L, 1L));
    }

    @Test
    void testGetItemByIdUserNotFoundException() {
        ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository,
                itemRepository);
        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.getById(1L, 1L));
    }

    @Test
    void testGetAllItemsByRequesterIdSuccess() throws UserNotFoundException, ItemRequestNotFoundException {
        User user = new User(1L, "User name", "user@gmail.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequestFirst = new ItemRequest(1L, "Item first description", 1L,
                created);
        ItemRequest itemRequestSecond = new ItemRequest(2L, "Item second description", 1L,
                created);
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequestFirst);
        itemRequestList.add(itemRequestSecond);
        ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository,
                itemRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRequestRepository.findByRequesterIdOrderByCreatedDesc(anyLong()))
                .thenReturn(itemRequestList);
        Mockito
                .when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequestFirst));

        List<ItemRequestDtoWithItems> itemRequestResult = itemRequestService.getByRequesterId(1L);
        assertThat(itemRequestResult.get(0).getId(), equalTo(itemRequestFirst.getId()));
        assertThat(itemRequestResult.get(0).getDescription(), equalTo(itemRequestFirst.getDescription()));
        assertThat(itemRequestResult.get(0).getCreated(), equalTo(itemRequestFirst.getCreated()));
    }
}
