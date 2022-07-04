package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        Long generatedId = User.setIdCounter();
        user.setId(generatedId);
        users.put(generatedId, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
