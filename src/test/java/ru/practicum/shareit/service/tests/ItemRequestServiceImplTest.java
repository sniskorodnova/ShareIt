package ru.practicum.shareit.service.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @Test
    public void checkGetRequestByIdSuccess() throws ItemRequestNotFoundException, UserNotFoundException,
            ValidationException {
        UserDto requester = new UserDto(1L, "User requester", "qwerty@gmail.com");
        userServiceImpl.create(requester);

        ItemRequestCreateDto itemRequestDto = new ItemRequestCreateDto("Request description");
        itemRequestServiceImpl.create(requester.getId(), itemRequestDto);
        ItemRequestDtoWithItems itemRequestDtoWithItems = new ItemRequestDtoWithItems(1L,
                "Request description", itemRequestServiceImpl.getById(1L, 1L).getCreated(),
                Collections.emptyList());
        assertThat(itemRequestServiceImpl.getById(1L, 1L), is(equalTo(itemRequestDtoWithItems)));
    }

    @Test
    public void checkExceptionIfUserNotFound() throws ItemRequestNotFoundException, UserNotFoundException,
            ValidationException {
        UserDto requester = new UserDto(1L, "User requester", "qwerty@gmail.com");
        userServiceImpl.create(requester);
        ItemRequestCreateDto itemRequestDto = new ItemRequestCreateDto("Request description");
        itemRequestServiceImpl.create(requester.getId(), itemRequestDto);

        Exception exception = assertThrows(UserNotFoundException.class, () ->
                itemRequestServiceImpl.getById(1L, 3L));
        String expectedMessage = "User with id = 3 not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void checkExceptionIfRequestNotFound() throws ItemRequestNotFoundException, UserNotFoundException,
            ValidationException {
        UserDto requester = new UserDto(1L, "User requester", "qwerty@gmail.com");
        userServiceImpl.create(requester);
        ItemRequestCreateDto itemRequestDto = new ItemRequestCreateDto("Request description");
        itemRequestServiceImpl.create(requester.getId(), itemRequestDto);

        Exception exception = assertThrows(ItemRequestNotFoundException.class, () ->
                itemRequestServiceImpl.getById(3L, 1L));
        String expectedMessage = "Item request with id = 3 not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void checkGetRequestByIdWithItemsSuccess() throws ItemRequestNotFoundException, UserNotFoundException,
            ValidationException {
        UserDto requester = new UserDto(1L, "User requester", "qwerty@gmail.com");
        userServiceImpl.create(requester);
        UserDto owner = new UserDto(1L, "User owner", "dfgh@gmail.com");
        userServiceImpl.create(owner);

        ItemRequestCreateDto itemRequestDto = new ItemRequestCreateDto("Request description");
        itemRequestServiceImpl.create(requester.getId(), itemRequestDto);

        ItemDto itemFirst = new ItemDto(1L, "Item name first",
                "Item description first", true, 1L);
        ItemCreateDto itemDtoFirst = new ItemCreateDto("Item name first",
                "Item description first", true, 1L);
        itemServiceImpl.create(owner.getId(), itemDtoFirst);

        ItemDto itemSecond = new ItemDto(2L, "Item name second",
                "Item description second", true, 1L);
        ItemCreateDto itemDtoSecond = new ItemCreateDto("Item name second",
                "Item description second", true, 1L);
        itemServiceImpl.create(owner.getId(), itemDtoSecond);

        List<ItemDto> listItems = new ArrayList<>();
        listItems.add(itemFirst);
        listItems.add(itemSecond);

        ItemRequestDtoWithItems itemRequestDtoWithItems = new ItemRequestDtoWithItems(1L,
                "Request description", itemRequestServiceImpl.getById(1L, 1L).getCreated(),
                listItems);
        assertThat(itemRequestServiceImpl.getById(1L, 1L), is(equalTo(itemRequestDtoWithItems)));
    }
}
