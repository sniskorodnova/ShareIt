package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если запрашиваемая вещь не найдена
 */
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
