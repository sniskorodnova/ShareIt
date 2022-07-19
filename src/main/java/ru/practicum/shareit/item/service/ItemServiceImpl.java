package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс, имплементирующий интерфейс для работы сервиса вещей
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Метод для создания вещи
     */
    @Override
    public Item create(Long userId, Item item) throws ValidationException, UserNotFoundException {
        if (userRepository.findById(userId).isPresent()) {
            item.setOwnerId(userId);
            return itemRepository.save(item);
        } else {
            throw new UserNotFoundException("No user with id = " + userId);
        }
    }

    /**
     * Метод для редактирования вещи
     */
    @Override
    public Item update(Long userId, Long id, Item item) throws ValidationException, AuthFailedException {
        Optional<Item> itemFromList = itemRepository.findById(id);
        userRepository.findById(userId);
        if (!Objects.equals(userId, itemFromList.get().getOwnerId())) {
            throw new AuthFailedException("Item doesn't belong to user with id = " + userId);
        } else {
            item.setOwnerId(userId);
            item.setId(id);
            if (item.getName() == null) {
                item.setName(itemFromList.get().getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(itemFromList.get().getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(itemFromList.get().getAvailable());
            }
            return itemRepository.save(item);
        }
    }

    /**
     * Метод для получения вещи по id
     */
    @Override
    public Item getById(Long userId, Long id) throws ItemNotFoundException, UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("No user with id = " + userId);
        } else {
            if (itemRepository.findById(id).isEmpty()) {
                throw new ItemNotFoundException("No item with id = " + id);
            } else {
                return itemRepository.findById(id).get();
            }
        }
    }

    /**
     * Метод для получения списка всех вещей
     */
    @Override
    public List<Item> getAll(Long userId) throws UserNotFoundException {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("No user with id = " + userId);
        } else {
            List<Item> itemToReturn = new ArrayList<>();
            for (Item item : itemRepository.findAll()) {
                if (Objects.equals(item.getOwnerId(), userId)) {
                    itemToReturn.add(item);
                }
            }
            return itemToReturn;
        }
    }

    /**
     * Метод для поиска вещей по буквосочетанию
     */
    @Override
    public List<Item> searchByText(Long userId, String searchText) throws UserNotFoundException {
        if (userRepository.findById(userId).isPresent()) {
            if (searchText.isEmpty()) {
                return Collections.emptyList();
            } else {
                return itemRepository.search(searchText);
            }
        } else {
            throw new UserNotFoundException("No user with id = " + userId);
        }
    }

    /**
     * Метод для создания отзыва к вещи
     */
    @Override
    public Comment createComment(Long userId, Long itemId, Comment comment) throws UserNotFoundException,
            ValidationException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("No user with id = " + userId);
        } else {
            System.out.println(bookingRepository.findForItem(itemId, userId).size());
            if (bookingRepository.findForItem(itemId, userId).size() == 0) {
                throw new ValidationException("User can't create comment for this item");
            } else {
                User user = userRepository.findById(userId).orElseThrow();
                Item item = itemRepository.findById(itemId).orElseThrow();
                comment.setItem(item);
                comment.setAuthor(user);
                comment.setCreated(LocalDate.now());
                return commentRepository.save(comment);
            }
        }
    }

    /**
     * Метод для получения списка отзывов на вещь
     */
    @Override
    public List<Comment> getCommentsForItem(Long itemId) {
        return commentRepository.findByItem_id(itemId);
    }

    /**
     * Метод для получения ближайшего прошедшего бронирования для вещи
     */
    @Override
    public Booking getLastBooking(Long itemId, Long userId) {
        if (bookingRepository.getLastBooking(itemId, userId).size() == 0) {
            return null;
        } else {
            return bookingRepository.getLastBooking(itemId, userId).get(0);
        }
    }

    /**
     * Метод для получения ближайшего будущего бронирования для вещи
     */
    @Override
    public Booking getNextBooking(Long itemId, Long userId) {
        if (bookingRepository.getNextBooking(itemId, userId).size() == 0) {
            return null;
        } else {
            return bookingRepository.getNextBooking(itemId, userId).get(0);
        }
    }
}
