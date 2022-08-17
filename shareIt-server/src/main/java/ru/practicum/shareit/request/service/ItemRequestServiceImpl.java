package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, имплементирующий интерфейс для работы сервиса запросов на вещи
 */
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository,
                                  ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Метод для создания запроса на вещь
     */
    @Override
    public ItemRequestDto create(Long userId, ItemRequestCreateDto itemRequestCreate) throws UserNotFoundException {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequestForCreate(itemRequestCreate);
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            itemRequest.setRequesterId(userId);
            itemRequest.setCreated(LocalDateTime.now());
            return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
        }
    }

    /**
     * Метод для получения запросов на вещи по id пользователя, который запрашивал вещи
     */
    @Override
    public List<ItemRequestDtoWithItems> getByRequesterId(Long userId) throws UserNotFoundException,
            ItemRequestNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            List<ItemRequestDtoWithItems> listItemRequestDtoWithItems = new ArrayList<>();
            for (ItemRequest itemRequest : itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId)) {
                ItemRequestDtoWithItems itemFound = getById(itemRequest.getId(), userId);
                listItemRequestDtoWithItems.add(itemFound);
            }
            return listItemRequestDtoWithItems;
        }
    }

    /**
     * Метод для получения запроса на вещь по его id
     */
    @Override
    public ItemRequestDtoWithItems getById(Long requestId, Long userId) throws UserNotFoundException,
            ItemRequestNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        } else {
            if (itemRequestRepository.findById(requestId).isEmpty()) {
                throw new ItemRequestNotFoundException("Item request with id = " + requestId + " not found");
            } else {
                List<ItemDto> itemsDto = new ArrayList<>();
                for (Item item : itemRepository.getItemsForRequest(requestId)) {
                    itemsDto.add(ItemMapper.toItemDto(item));
                }
                return ItemRequestMapper
                        .toItemRequestDtoWithItems(itemRequestRepository.findById(requestId).orElseThrow(), itemsDto);
            }
        }
    }

    /**
     * Метод для получения списка запросов на все вещи
     */
    @Override
    public List<ItemRequestDtoWithItems> getAllWithPagination(Long userId, Integer from, Integer size)
            throws UserNotFoundException, ItemRequestNotFoundException {
        List<ItemRequestDtoWithItems> listItemRequestDtoWithItems = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId,
                PageRequest.of(from / size, size))) {
            ItemRequestDtoWithItems itemFound = getById(itemRequest.getId(), userId);
            listItemRequestDtoWithItems.add(itemFound);
        }
        return listItemRequestDtoWithItems;
    }
}
