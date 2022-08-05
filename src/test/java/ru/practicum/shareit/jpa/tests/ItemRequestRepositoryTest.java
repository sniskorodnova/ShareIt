package ru.practicum.shareit.jpa.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
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
public class ItemRequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void findRequestListForUserOrderByCreatedDesc() {
        User userRequester = new User(1L, "User requester", "kjgd@gmail.com");
        userRepository.save(userRequester);

        LocalDateTime createdFirst = LocalDateTime.now().minusDays(1L);
        ItemRequest itemRequestFirst = new ItemRequest(1L, "Request description first",
                userRequester.getId(), createdFirst);
        itemRequestRepository.save(itemRequestFirst);

        LocalDateTime createdSecond = LocalDateTime.now().minusDays(2L);
        ItemRequest itemRequestSecond = new ItemRequest(2L, "Request description second",
                userRequester.getId(), createdSecond);
        itemRequestRepository.save(itemRequestSecond);

        List<ItemRequest> foundRequests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(1L);
        List<ItemRequest> listToCompare = new ArrayList<>();
        listToCompare.add(itemRequestFirst);
        listToCompare.add(itemRequestSecond);
        assertThat(foundRequests, is(equalTo(listToCompare)));
    }

    @Test
    public void findRequestListForNotUser() {
        User userRequesterFirst = new User(1L, "User requester first", "kjgd@gmail.com");
        userRepository.save(userRequesterFirst);
        User userRequesterSecond = new User(2L, "User requester second", "qwerty@gmail.com");
        userRepository.save(userRequesterSecond);

        LocalDateTime createdFirst = LocalDateTime.now().minusDays(1L);
        ItemRequest itemRequestFirst = new ItemRequest(1L, "Request description first",
                userRequesterFirst.getId(), createdFirst);
        itemRequestRepository.save(itemRequestFirst);

        LocalDateTime createdSecond = LocalDateTime.now().minusDays(2L);
        ItemRequest itemRequestSecond = new ItemRequest(2L, "Request description second",
                userRequesterSecond.getId(), createdSecond);
        itemRequestRepository.save(itemRequestSecond);

        List<ItemRequest> foundRequests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(1L,
                PageRequest.of(0, 5));
        List<ItemRequest> listToCompare = new ArrayList<>();
        listToCompare.add(itemRequestSecond);
        assertThat(foundRequests, is(equalTo(listToCompare)));
    }

    @Test
    public void findRequestListForNotUserOrderByCreatedDesc() {
        User userRequesterFirst = new User(1L, "User requester first", "kjgd@gmail.com");
        userRepository.save(userRequesterFirst);
        User userRequesterSecond = new User(2L, "User requester second", "qwerty@gmail.com");
        userRepository.save(userRequesterSecond);

        LocalDateTime createdFirst = LocalDateTime.now().minusDays(1L);
        ItemRequest itemRequestFirst = new ItemRequest(1L, "Request description first",
                userRequesterFirst.getId(), createdFirst);
        itemRequestRepository.save(itemRequestFirst);

        LocalDateTime createdSecond = LocalDateTime.now().minusDays(2L);
        ItemRequest itemRequestSecond = new ItemRequest(2L, "Request description second",
                userRequesterSecond.getId(), createdSecond);
        itemRequestRepository.save(itemRequestSecond);

        List<ItemRequest> foundRequests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(3L,
                PageRequest.of(0, 5));
        List<ItemRequest> listToCompare = new ArrayList<>();
        listToCompare.add(itemRequestFirst);
        listToCompare.add(itemRequestSecond);
        assertThat(foundRequests, is(equalTo(listToCompare)));
    }
}
