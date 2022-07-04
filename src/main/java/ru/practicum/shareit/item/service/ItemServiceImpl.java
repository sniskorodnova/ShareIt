package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AuthFailedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

/**
 * Класс, имплементирующий интерфейс для работы сервиса вещей
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    /**
     * Метод для создания вещи
     */
    @Override
    public Item create(Long userId, Item item) throws ValidationException {
        if (userStorage.getById(userId) != null) {
            item.setOwner(userId);
            return itemStorage.create(item);
        } else {
            throw new ValidationException("No user with id = " + userId);
        }
    }

    /**
     * Метод для редактирования вещи
     */
    @Override
    public Item update(Long userId, Long id, Item item) throws ValidationException, AuthFailedException {
        Item itemFromList = itemStorage.getById(id);
        if (userStorage.getById(userId) == null) {
            throw new ValidationException("No user with id = " + userId);
        } else if (!Objects.equals(userId, itemFromList.getOwner())) {
            throw new AuthFailedException("Item doesn't belong to user with id = " + userId);
        } else {
            item.setOwner(userId);
            item.setId(id);
            if (item.getName() == null) {
                item.setName(itemFromList.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(itemFromList.getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(itemFromList.getAvailable());
            }
            return itemStorage.update(item);
        }
    }

    /**
     * Метод для получения вещи по id
     */
    @Override
    public Item getById(Long userId, Long id) throws ValidationException {
        if (userStorage.getById(userId) == null) {
            throw new ValidationException("No user with id = " + userId);
        } else {
            return itemStorage.getById(id);
        }
    }

    /**
     * Метод для получения списка всех вещей
     */
    @Override
    public List<Item> getAll(Long userId) throws ValidationException {
        if (userStorage.getById(userId) == null) {
            throw new ValidationException("No user with id = " + userId);
        } else {
            List<Item> itemToReturn = new ArrayList<>();
            for (Item item : itemStorage.getAll()) {
                if (Objects.equals(item.getOwner(), userId)) {
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
    public List<Item> searchByText(Long userId, String searchText) throws ValidationException {
        if (userStorage.getById(userId) != null) {
            List<Item> foundItems = new ArrayList<>();
            if (searchText.isEmpty()) {
                return Collections.emptyList();
            } else {
                for (Item item : itemStorage.getAll()) {
                    if ((item.getName().toLowerCase().contains(searchText.toLowerCase())
                            || item.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                            && item.getAvailable()) {
                        foundItems.add(item);
                    }
                }
                return foundItems;
            }
        } else {
            throw new ValidationException("No user with id = " + userId);
        }
    }
}
