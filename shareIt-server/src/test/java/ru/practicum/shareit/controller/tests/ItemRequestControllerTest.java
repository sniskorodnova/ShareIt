package ru.practicum.shareit.controller.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    public void shouldReturnRequestInfoAfterCreating() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto("Item request description");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Item request description", created);
        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests").content(mapper.writeValueAsString(itemRequestCreateDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created",
                        is((itemRequestDto.getCreated().toString()).replaceAll("0+$", ""))));
    }

    @Test
    public void shouldReturnRequestInfoByRequesterId() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        List<ItemRequestDtoWithItems> listRequests = new ArrayList<>();
        ItemRequestDtoWithItems itemRequestDtoWithItems = new ItemRequestDtoWithItems(1L,
                "Item request description", created, null);
        listRequests.add(itemRequestDtoWithItems);

        when(itemRequestService.getByRequesterId(anyLong()))
                .thenReturn(listRequests);
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoWithItems.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoWithItems.getDescription())))
                .andExpect(jsonPath("$[0].created",
                        is((itemRequestDtoWithItems.getCreated().toString()).replaceAll("0+$", ""))))
                .andExpect(jsonPath("$[0].items", is(itemRequestDtoWithItems.getItems())));
    }

    @Test
    public void shouldReturnRequestInfoById() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        List<ItemRequestDtoWithItems> listRequests = new ArrayList<>();
        ItemRequestDtoWithItems itemRequestDtoWithItems = new ItemRequestDtoWithItems(1L,
                "Item request description", created, null);

        when(itemRequestService.getById(anyLong(), anyLong()))
                .thenReturn(itemRequestDtoWithItems);
        mockMvc.perform(get("/requests/" + itemRequestDtoWithItems.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoWithItems.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoWithItems.getDescription())))
                .andExpect(jsonPath("$.created",
                        is((itemRequestDtoWithItems.getCreated().toString()).replaceAll("0+$", ""))))
                .andExpect(jsonPath("$.items", is(itemRequestDtoWithItems.getItems())));
    }

    @Test
    public void shouldReturnRequestsInfoGetAll() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        List<ItemRequestDtoWithItems> listRequests = new ArrayList<>();
        ItemRequestDtoWithItems itemRequestDtoWithItems = new ItemRequestDtoWithItems(1L,
                "Item request description", created, null);

        when(itemRequestService.getById(anyLong(), anyLong()))
                .thenReturn(itemRequestDtoWithItems);
        mockMvc.perform(get("/requests/" + itemRequestDtoWithItems.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoWithItems.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoWithItems.getDescription())))
                .andExpect(jsonPath("$.created",
                        is((itemRequestDtoWithItems.getCreated().toString()).replaceAll("0+$", ""))))
                .andExpect(jsonPath("$.items", is(itemRequestDtoWithItems.getItems())));
    }
}
