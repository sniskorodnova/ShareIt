package ru.practicum.shareit.unit.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Test
    void testCreateUserSuccess() throws ValidationException {
        User user = new User(1L, "User name", "user@gmail.com");
        UserDto userDto = new UserDto(1L, "User name", "user@gmail.com");
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Mockito
                .when(userRepository.save(any(User.class)))
                .thenReturn(user);
        UserDto userDtoResult = userService.create(userDto);
        assertThat(userDtoResult.getId(), equalTo(userDto.getId()));
        assertThat(userDtoResult.getName(), equalTo(userDto.getName()));
        assertThat(userDtoResult.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void testUpdateUserSuccess() throws EmailAlreadyExistsException {
        User userUpdated = new User(1L, "User update name", "userUpdate@gmail.com");
        UserDto userUpdatedDto = new UserDto(1L, "User update name", "userUpdate@gmail.com");
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Mockito
                .when(userRepository.save(any(User.class)))
                .thenReturn(userUpdated);
        UserDto userDtoResult = userService.update(1L, userUpdatedDto);
        assertThat(userDtoResult.getId(), equalTo(userUpdatedDto.getId()));
        assertThat(userDtoResult.getName(), equalTo(userUpdatedDto.getName()));
        assertThat(userDtoResult.getEmail(), equalTo(userUpdatedDto.getEmail()));
    }

    @Test
    void testUpdateUserEmailAlreadyExistsException() {
        User user = new User(1L, "User name", "user@gmail.com");
        UserDto userUpdatedDto = new UserDto(1L, "User update name", "userUpdate@gmail.com");
        User userWithEmail = new User(2L, "User 3", "userUpdate@gmail.com");
        List<User> listUser = new ArrayList<>();
        listUser.add(user);
        listUser.add(userWithEmail);
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(userRepository.findAll())
                .thenReturn(listUser);
        assertThrows(EmailAlreadyExistsException.class, () ->
                userService.update(1L, userUpdatedDto));
    }

    @Test
    void testGetUserByIdSuccess() throws UserNotFoundException {
        User user = new User(1L, "User name", "user@gmail.com");
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        UserDto userDtoResult = userService.getById(1L);
        assertThat(userDtoResult.getId(), equalTo(user.getId()));
        assertThat(userDtoResult.getName(), equalTo(user.getName()));
        assertThat(userDtoResult.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void testGetUserByIdUserNotFoundException() throws UserNotFoundException {
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        assertThrows(UserNotFoundException.class, () ->
                userService.getById(1L));
    }

    @Test
    void testGetAllUsersSuccess() {
        User userFirst = new User(1L, "User first name", "userFirst@gmail.com");
        User userSecond = new User(2L, "User second name", "userSecond@gmail.com");
        List<User> listUsers = new ArrayList<>();
        listUsers.add(userFirst);
        listUsers.add(userSecond);
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Mockito
                .when(userRepository.findAll())
                .thenReturn(listUsers);
        List<UserDto> userDtoResult = userService.getAll();
        assertThat(userDtoResult.get(0).getId(), equalTo(userFirst.getId()));
        assertThat(userDtoResult.get(0).getName(), equalTo(userFirst.getName()));
        assertThat(userDtoResult.get(0).getEmail(), equalTo(userFirst.getEmail()));
        assertThat(userDtoResult.get(1).getId(), equalTo(userSecond.getId()));
        assertThat(userDtoResult.get(1).getName(), equalTo(userSecond.getName()));
        assertThat(userDtoResult.get(1).getEmail(), equalTo(userSecond.getEmail()));
    }
}
