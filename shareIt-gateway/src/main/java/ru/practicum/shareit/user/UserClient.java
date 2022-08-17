package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareIt-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object create(UserDto user) {
        return post("", null, user);
    }

    public Object update(Long id, UserDto user) {
        return patch("/" + id, id, user);
    }

    public Object getById(Long id) {
        return get("/" + id, null);
    }

    public Object getAll() {
        return get("", null);
    }

    public Object deleteById(Long id) {
        return delete("/" + id);
    }
}
