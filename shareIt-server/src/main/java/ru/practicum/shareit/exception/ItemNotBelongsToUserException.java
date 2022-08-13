package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если запрашиваемая вещь не принадлежит пользователю
 */
public class ItemNotBelongsToUserException extends Exception {
    public ItemNotBelongsToUserException(String message) {
        super(message);
    }
}
