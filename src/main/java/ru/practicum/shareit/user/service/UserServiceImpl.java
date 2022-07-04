package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User create(User user) throws ValidationException {
        for (User userToFind : userStorage.getAll()) {
            if(userToFind.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }
        return userStorage.create(user);
    }

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

    @Override
    public User getById(Long id) {
        return userStorage.getById(id);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }
}
