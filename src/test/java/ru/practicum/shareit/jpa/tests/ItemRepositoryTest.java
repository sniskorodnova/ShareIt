package ru.practicum.shareit.jpa.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
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
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findAllItemsOrderByIdAsc() {
        User userOwnerFirst = new User(1L, "User owner first", "kjgd@gmail.com");
        userRepository.save(userOwnerFirst);
        User userOwnerSecond = new User(2L, "User owner second", "csdfds@gmail.com");
        userRepository.save(userOwnerSecond);

        Item itemFirst = new Item(1L, "Item first", "Item description first", true,
                1L, null);
        itemRepository.save(itemFirst);
        Item itemSecond = new Item(2L, "Item second", "Item description second", false,
                2L, null);
        itemRepository.save(itemSecond);
        Item itemThird = new Item(3L, "Item third", "Item description third", true,
                2L, null);
        itemRepository.save(itemThird);

        List<Item> foundItems = itemRepository.findAllByOrderByIdAsc(PageRequest.of(0, 5));
        List<Item> listToCompare = new ArrayList<>();
        listToCompare.add(itemFirst);
        listToCompare.add(itemSecond);
        listToCompare.add(itemThird);
        assertThat(foundItems, is(equalTo(listToCompare)));
    }

    @Test
    public void searchItemByText() {
        User userOwnerFirst = new User(1L, "User owner first", "kjgd@gmail.com");
        userRepository.save(userOwnerFirst);
        User userOwnerSecond = new User(2L, "User owner second", "csdfds@gmail.com");
        userRepository.save(userOwnerSecond);

        Item itemFirst = new Item(1L, "Item first", "Item description first", true,
                1L, null);
        itemRepository.save(itemFirst);
        Item itemSecond = new Item(2L, "Item second", "Item description second", false,
                2L, null);
        itemRepository.save(itemSecond);
        Item itemThird = new Item(3L, "Item third", "Item description third", true,
                2L, null);
        itemRepository.save(itemThird);

        List<Item> foundItems = itemRepository.search("ItEm Firs", PageRequest.of(0, 5));
        List<Item> listToCompare = new ArrayList<>();
        listToCompare.add(itemFirst);
        assertThat(foundItems, is(equalTo(listToCompare)));
    }

    @Test
    public void findItemsForRequest() {
        User userOwnerFirst = new User(1L, "User owner first", "kjgd@gmail.com");
        userRepository.save(userOwnerFirst);
        User userOwnerSecond = new User(2L, "User owner second", "csdfds@gmail.com");
        userRepository.save(userOwnerSecond);

        LocalDateTime created = LocalDateTime.now().minusDays(1L);
        ItemRequest itemRequest = new ItemRequest(1L, "Request description",
                userOwnerFirst.getId(), created);
        itemRequestRepository.save(itemRequest);

        Item itemFirst = new Item(1L, "Item first", "Item description first", true,
                1L, 1L);
        itemRepository.save(itemFirst);
        Item itemSecond = new Item(2L, "Item second", "Item description second", false,
                2L, 1L);
        itemRepository.save(itemSecond);
        Item itemThird = new Item(3L, "Item third", "Item description third", true,
                2L, null);
        itemRepository.save(itemThird);

        List<Item> foundItems = itemRepository.getItemsForRequest(1L);
        List<Item> listToCompare = new ArrayList<>();
        listToCompare.add(itemFirst);
        listToCompare.add(itemSecond);
        assertThat(foundItems, is(equalTo(listToCompare)));
    }
}
