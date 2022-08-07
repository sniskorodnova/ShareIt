package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если запрос на вещь не найден
 */
public class ItemRequestNotFoundException extends Exception {
    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}
