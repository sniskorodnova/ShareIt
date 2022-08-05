package ru.practicum.shareit.controller.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Test
    public void shouldReturnItemInfoAfterCreating() throws Exception {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Item name", "Item description", true,
                1L);
        ItemDto itemDto = new ItemDto(1L, "Item name", "Item description", true,
                1L);

        when(itemService.create(anyLong(), any()))
                .thenReturn(itemDto);
        mockMvc.perform(post("/items").header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is((itemDto.getName()))))
                .andExpect(jsonPath("$.description", is((itemDto.getDescription()))))
                .andExpect(jsonPath("$.available", is((itemDto.getAvailable()))))
                .andExpect(jsonPath("$.requestId", is((itemDto.getRequestId())), Long.class));
    }

    @Test
    public void shouldReturnUpdatedItemInfoAfterPatching() throws Exception {
        ItemCreateDto itemUpdatedCreateDto = new ItemCreateDto("Item update name",
                "Item update description", true, 1L);
        ItemDto itemDto = new ItemDto(1L, "Item update name", "Item update description",
                true, 1L);

        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);
        mockMvc.perform(patch("/items/" + itemDto.getId()).header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemUpdatedCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is((itemDto.getName()))))
                .andExpect(jsonPath("$.description", is((itemDto.getDescription()))))
                .andExpect(jsonPath("$.available", is((itemDto.getAvailable()))))
                .andExpect(jsonPath("$.requestId", is((itemDto.getRequestId())), Long.class));
    }

    @Test
    public void shouldReturnItemInfoById() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Item name", "Item description", true,
                1L);
        ItemDtoWithComment itemDtoWithComment = new ItemDtoWithComment(1L, "Item name",
                "Item description", true, 1L, null, null,
                null);

        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDtoWithComment);
        mockMvc.perform(get("/items/" + itemDto.getId()).header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is((itemDto.getName()))))
                .andExpect(jsonPath("$.description", is((itemDto.getDescription()))))
                .andExpect(jsonPath("$.available", is((itemDto.getAvailable()))))
                .andExpect(jsonPath("$.requestId", is((itemDto.getRequestId())), Long.class));
    }

    @Test
    public void shouldReturnAllItemsInfo() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Item name", "Item description", true,
                1L);
        ItemDtoWithComment itemDtoWithCommentFirst = new ItemDtoWithComment(1L, "Item first name",
                "Item first description", true, 1L, null, null,
                null);
        ItemDtoWithComment itemDtoWithCommentSecond = new ItemDtoWithComment(1L, "Item second name",
                "Item second description", false, 1L, null, null,
                null);
        List<ItemDtoWithComment> listItems = new ArrayList<>();
        listItems.add(itemDtoWithCommentFirst);
        listItems.add(itemDtoWithCommentSecond);

        when(itemService.getAllWithPagination(anyLong(), anyInt(), anyInt()))
                .thenReturn(listItems);
        mockMvc.perform(get("/items").header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoWithCommentFirst.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is((itemDtoWithCommentFirst.getName()))))
                .andExpect(jsonPath("$[0].description", is((itemDtoWithCommentFirst.getDescription()))))
                .andExpect(jsonPath("$[0].available", is((itemDtoWithCommentFirst.getAvailable()))))
                .andExpect(jsonPath("$[0].requestId", is((itemDtoWithCommentFirst.getRequestId())),
                        Long.class))
                .andExpect(jsonPath("$[1].id", is(itemDtoWithCommentSecond.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is((itemDtoWithCommentSecond.getName()))))
                .andExpect(jsonPath("$[1].description", is((itemDtoWithCommentSecond.getDescription()))))
                .andExpect(jsonPath("$[1].available", is((itemDtoWithCommentSecond.getAvailable()))))
                .andExpect(jsonPath("$[1].requestId", is((itemDtoWithCommentSecond.getRequestId())),
                        Long.class));
    }

    @Test
    public void shouldReturnItemsFoundByText() throws Exception {
        ItemDto itemDtoFirst = new ItemDto(1L, "Item name first", "Item description first",
                true, 1L);
        ItemDto itemDtoSecond = new ItemDto(1L, "Item name second", "Item description second",
                false, 1L);
        List<ItemDto> listItems = new ArrayList<>();
        listItems.add(itemDtoFirst);
        listItems.add(itemDtoSecond);

        when(itemService.searchByTextWithPagination(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(listItems);
        mockMvc.perform(get("/items/search?text=item&from=0&size=5")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoFirst.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is((itemDtoFirst.getName()))))
                .andExpect(jsonPath("$[0].description", is((itemDtoFirst.getDescription()))))
                .andExpect(jsonPath("$[0].available", is((itemDtoFirst.getAvailable()))))
                .andExpect(jsonPath("$[0].requestId", is((itemDtoFirst.getRequestId())),
                        Long.class))
                .andExpect(jsonPath("$[1].id", is(itemDtoSecond.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is((itemDtoSecond.getName()))))
                .andExpect(jsonPath("$[1].description", is((itemDtoSecond.getDescription()))))
                .andExpect(jsonPath("$[1].available", is((itemDtoSecond.getAvailable()))))
                .andExpect(jsonPath("$[1].requestId", is((itemDtoSecond.getRequestId())),
                        Long.class));
    }

    @Test
    public void shouldReturnCommentAfterCreatingSuccess() throws Exception {
        Item item = new Item(1L, "Item name", "Item description", true, 1L,
                1L);
        CommentCreateDto commentCreateDto = new CommentCreateDto("Comment text");
        User user = new User(1L, "User name", "qwe@gmail.com");
        LocalDate created = LocalDate.now();
        CommentDto commentDto = new CommentDto(1L, "Comment text", item, "User name", created);

        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);
        mockMvc.perform(post("/items/" + item.getId() + "/comment")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(commentCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is((commentDto.getText()))))
                .andExpect(jsonPath("$.created", is((commentDto.getCreated().toString()))))
                .andExpect(jsonPath("$.authorName", is((commentDto.getAuthorName()))));
    }
}
