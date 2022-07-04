package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Класс, описывающий модель вещь
 */
@Data
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private ItemRequest request;
    private static AtomicLong counter = new AtomicLong(0);

    public static Long setIdCounter() {
        return counter.incrementAndGet();
    }
}
