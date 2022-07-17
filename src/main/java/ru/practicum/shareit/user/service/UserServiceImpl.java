package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс, имплементирующий интерфейс для работы сервиса пользователей
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Метод для создания пользователя
     */
    @Override
    public User create(User user) throws ValidationException {
        return userRepository.save(user);
    }

    /**
     * Метод для редактирования пользователя
     */
    @Override
    public User update(Long id, User user) throws ValidationException {
        Optional<User> userFromList = userRepository.findById(id);
        user.setId(id);
        if (user.getName() == null) {
            user.setName(userFromList.get().getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userFromList.get().getEmail());
        }
        for (User userToFind : userRepository.findAll()) {
            if ((!Objects.equals(userToFind.getId(), user.getId())) && userToFind.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }
        return userRepository.save(user);
    }

    /**
     * Метод для получения пользователя по id
     */
    @Override
    public User getById(Long id) throws UserNotFoundException {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id = " + id + " doesn't exist");
        } else {
            return userRepository.findById(id).get();
        }
    }

    /**
     * Метод для получения списка всех пользователей
     */
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Метод для удаления пользователя по id
     */
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
