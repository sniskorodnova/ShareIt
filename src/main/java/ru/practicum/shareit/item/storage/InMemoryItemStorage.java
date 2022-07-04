package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private HashMap<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        Long generatedId = Item.setIdCounter();
        item.setId(generatedId);
        items.put(generatedId, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item getById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }
}
