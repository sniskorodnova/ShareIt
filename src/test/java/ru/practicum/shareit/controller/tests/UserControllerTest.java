package ru.practicum.shareit.controller.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnUserInfoAfterCreating() throws Exception {
        UserDto userDto = new UserDto(1L, "User name", "qwe@gmail.com");
        when(userService.create(any()))
                .thenReturn(userDto);
        mockMvc.perform(post("/users").content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void shouldReturnUpdatedUserInfoAfterPatching() throws Exception {
        UserDto userDto = new UserDto(1L, "User name", "qwe@gmail.com");
        UserDto userUpdatedDto = new UserDto(1L, "User name updated", "qweUpdated@gmail.com");
        when(userService.update(anyLong(), any()))
                .thenReturn(userUpdatedDto);
        mockMvc.perform(patch("/users/" + userDto.getId()).content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userUpdatedDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userUpdatedDto.getName())))
                .andExpect(jsonPath("$.email", is(userUpdatedDto.getEmail())));
    }

    @Test
    public void shouldReturnUserInfoById() throws Exception {
        UserDto userDto = new UserDto(1L, "User name", "qwe@gmail.com");
        when(userService.getById(anyLong()))
                .thenReturn(userDto);
        mockMvc.perform(get("/users/" + userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void shouldReturnInfoForAllUsers() throws Exception {
        UserDto userDtoFirst = new UserDto(1L, "User name first", "qwe@gmail.com");
        UserDto userDtoSecond = new UserDto(2L, "User name second", "rewq@gmail.com");
        List<UserDto> listUsers = new ArrayList<>();
        listUsers.add(userDtoFirst);
        listUsers.add(userDtoSecond);
        when(userService.getAll())
                .thenReturn(listUsers);
        mockMvc.perform(get("/users/")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userDtoFirst.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDtoFirst.getName())))
                .andExpect(jsonPath("$[0].email", is(userDtoFirst.getEmail())))
                .andExpect(jsonPath("$[1].id", is(userDtoSecond.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(userDtoSecond.getName())))
                .andExpect(jsonPath("$[1].email", is(userDtoSecond.getEmail())));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        UserDto userDto = new UserDto(1L, "User name", "qwe@gmail.com");
        mockMvc.perform(get("/users/" + userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
