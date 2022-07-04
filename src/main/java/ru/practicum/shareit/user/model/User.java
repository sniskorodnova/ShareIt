package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Класс, описывающий модель пользователя
 */
@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private static AtomicLong counter = new AtomicLong(0);

    public static Long setIdCounter() {
        return counter.incrementAndGet();
    }
}
