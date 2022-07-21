package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
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
    public UserDto create(UserDto user) throws ValidationException {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    /**
     * Метод для редактирования пользователя
     */
    @Override
    public UserDto update(Long id, UserDto user) throws EmailAlreadyExistsException {
        Optional<User> userFromList = userRepository.findById(id);
        user.setId(id);

        if (user.getName() == null) {
            user.setName(userFromList.orElseThrow().getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userFromList.orElseThrow().getEmail());
        }
        for (User userToFind : userRepository.findAll()) {
            if ((!Objects.equals(userToFind.getId(), user.getId())) && userToFind.getEmail().equals(user.getEmail())) {
                throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
            }
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    /**
     * Метод для получения пользователя по id
     */
    @Override
    public UserDto getById(Long id) throws UserNotFoundException {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id = " + id + " doesn't exist");
        } else {
            return UserMapper.toUserDto(userRepository.findById(id).get());
        }
    }

    /**
     * Метод для получения списка всех пользователей
     */
    @Override
    public List<UserDto> getAll() {
        List<UserDto> listUserDto = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            listUserDto.add(UserMapper.toUserDto(user));
        }
        return listUserDto;
    }

    /**
     * Метод для удаления пользователя по id
     */
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
