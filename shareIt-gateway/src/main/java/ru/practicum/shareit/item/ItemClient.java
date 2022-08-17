package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareIt-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object create(Long userId, ItemCreateDto itemCreateDto) {
        return post("", userId, itemCreateDto);
    }

    public Object update(Long userId, Long itemId, ItemCreateDto itemCreateDto) {
        return patch("/" + itemId, userId, itemCreateDto);
    }

    public Object getById(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public Object getAll(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("" + "?from=" + from + "&size=" + size, userId, parameters);
    }

    public Object searchByText(Long userId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search" + "?text=" + text + "&from=" + from + "&size=" + size, userId, parameters);
    }

    public Object createComment(Long userId, Long itemId, CommentCreateDto commentCreateDto) {
        return post("/" + itemId + "/comment", userId, commentCreateDto);
    }
}
