package ru.practicum.shareit.service.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImplTest {
    private final UserServiceImpl userServiceImpl;

    @Test
    public void checkGetUserByIdSuccess() throws ValidationException, UserNotFoundException {
        UserDto userDto = new UserDto(1L, "User name", "qwerty@gmail.com");
        userServiceImpl.create(userDto);
        assertThat(userServiceImpl.getById(1L), is(equalTo(userDto)));
    }

    @Test
    public void checkExceptionIfUserNotFound() {
        Exception exception = assertThrows(UserNotFoundException.class, () ->
                userServiceImpl.getById(2L));
        String expectedMessage = "User with id = 2 doesn't exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
