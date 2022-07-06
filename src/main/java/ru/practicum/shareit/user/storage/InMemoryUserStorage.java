package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс, имплементирующий интерфейс для сохранения данных по пользователям в памяти
 */
@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();

    /**
     * Метод для создания пользователя
     */
    @Override
    public User create(User user) {
        Long generatedId = User.setIdCounter();
        user.setId(generatedId);
        users.put(generatedId, user);
        return user;
    }

    /**
     * Метод для редактирования пользователя
     */
    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    /**
     * Метод для получения пользователя по id
     */
    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    /**
     * Метод для удаления пользователя по id
     */
    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    /**
     * Метод для получения списка всех пользователей
     */
    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
