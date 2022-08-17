package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.*;
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
    public ItemDto create(Long userId, ItemCreateDto itemCreate) throws ValidationException, UserNotFoundException {
        Item item = ItemMapper.toItemForCreate(itemCreate);
        if (userRepository.findById(userId).isPresent()) {
            item.setOwnerId(userId);
            return ItemMapper.toItemDto(itemRepository.save(item));
        } else {
            throw new UserNotFoundException("No user with id = " + userId);
        }
    }

    /**
     * Метод для редактирования вещи
     */
    @Override
    public ItemDto update(Long userId, Long id, ItemCreateDto itemCreate) throws ValidationException,
            AuthFailedException {
        Item item = ItemMapper.toItemForCreate(itemCreate);
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
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
    }

    /**
     * Метод для получения вещи по id
     */
    @Override
    public ItemDtoWithComment getById(Long userId, Long id) throws ItemNotFoundException, UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("No user with id = " + userId);
        } else {
            if (itemRepository.findById(id).isEmpty()) {
                throw new ItemNotFoundException("No item with id = " + id);
            } else {
                List<CommentForItemDto> comments = new ArrayList<>();
                for (Comment comment : commentRepository.findByItem_id(id)) {
                    comments.add(CommentMapper.toCommentForItemDto(comment));
                }
                BookingDtoItem lastBooking = (getLastBooking(id, userId) == null) ? null
                        : BookingMapper.toBookingDtoItem(Objects.requireNonNull(getLastBooking(id, userId)));
                BookingDtoItem nextBooking = (getNextBooking(id, userId)) == null ? null
                        : BookingMapper.toBookingDtoItem(Objects.requireNonNull(getNextBooking(id, userId)));
                return ItemMapper.toItemDtoWithComment(itemRepository.findById(id).get(), comments, lastBooking,
                        nextBooking);
            }
        }
    }

    /**
     * Метод для получения списка всех вещей
     */
    @Override
    public List<ItemDtoWithComment> getAllWithPagination(Long userId, Integer from, Integer size)
            throws UserNotFoundException {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("No user with id = " + userId);
        } else {
            List<Item> itemToReturn = new ArrayList<>();
            for (Item item : itemRepository.findAllByOrderByIdAsc(PageRequest.of(from / size, size))) {
                if (Objects.equals(item.getOwnerId(), userId)) {
                    itemToReturn.add(item);
                }
            }
            List<ItemDtoWithComment> listItemCommentDto = new ArrayList<>();
            for (Item item : itemToReturn) {
                List<CommentForItemDto> comments = new ArrayList<>();
                for (Comment comment : commentRepository.findByItem_id(item.getId())) {
                    comments.add(CommentMapper.toCommentForItemDto(comment));
                }
                BookingDtoItem lastBooking = (getLastBooking(item.getId(), userId)) == null ? null
                        : BookingMapper.toBookingDtoItem(Objects.requireNonNull(getLastBooking(item.getId(), userId)));
                BookingDtoItem nextBooking = (getNextBooking(item.getId(), userId)) == null ? null
                        : BookingMapper.toBookingDtoItem(Objects.requireNonNull(getNextBooking(item.getId(), userId)));
                listItemCommentDto.add(ItemMapper.toItemDtoWithComment(item, comments, lastBooking, nextBooking));
            }
            return listItemCommentDto;
        }
    }

    /**
     * Метод для поиска вещей по буквосочетанию
     */
    @Override
    public List<ItemDto> searchByTextWithPagination(Long userId, String searchText, Integer from, Integer size)
            throws UserNotFoundException {
        if (userRepository.findById(userId).isPresent()) {
            if (searchText.isEmpty()) {
                return Collections.emptyList();
            } else {
                List<Item> foundItems = itemRepository.search(searchText, PageRequest.of(from / size, size));

                List<ItemDto> listItemDto = new ArrayList<>();
                if (foundItems == null) {
                    return null;
                } else {
                    for (Item item : foundItems) {
                        listItemDto.add(ItemMapper.toItemDto(item));
                    }
                    return listItemDto;
                }
            }
        } else {
            throw new UserNotFoundException("No user with id = " + userId);
        }
    }

    /**
     * Метод для создания отзыва к вещи
     */
    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentCreateDto commentCreate)
            throws UserNotFoundException, ValidationException {
        Comment comment = CommentMapper.toCommentForCreate(commentCreate);
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
                return CommentMapper.toCommentDto(commentRepository.save(comment));
            }
        }
    }

    /**
     * Метод для получения ближайшего прошедшего бронирования для вещи
     */
    private Booking getLastBooking(Long itemId, Long userId) {
        if (bookingRepository.getLastBooking(itemId, userId).size() == 0) {
            return null;
        } else {
            return bookingRepository.getLastBooking(itemId, userId).get(0);
        }
    }

    /**
     * Метод для получения ближайшего будущего бронирования для вещи
     */
    private Booking getNextBooking(Long itemId, Long userId) {
        if (bookingRepository.getNextBooking(itemId, userId).size() == 0) {
            return null;
        } else {
            return bookingRepository.getNextBooking(itemId, userId).get(0);
        }
    }
}
