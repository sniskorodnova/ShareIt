package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;

/**
 * Класс, имплементирующий интерфейс для работы сервиса пользователей
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Метод для создания пользователя
     */
    @Override
    public User create(User user) throws ValidationException {
        for (User userToFind : userStorage.getAll()) {
            if(userToFind.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }
        return userStorage.create(user);
    }

    /**
     * Метод для редактирования пользователя
     */
    @Override
    public User update(Long id, User user) throws ValidationException {
        User userFromList = userStorage.getById(id);
        user.setId(id);
        if (user.getName() == null) {
            user.setName(userFromList.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userFromList.getEmail());
        }
        for (User userToFind : userStorage.getAll()) {
            if((!Objects.equals(userToFind.getId(), user.getId())) && userToFind.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }
        return userStorage.update(user);
    }

    /**
     * Метод для получения пользователя по id
     */
    @Override
    public User getById(Long id) {
        return userStorage.getById(id);
    }

    /**
     * Метод для получения списка всех пользователей
     */
    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    /**
     * Метод для удаления пользователя по id
     */
    @Override
    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }
}
