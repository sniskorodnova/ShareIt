package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если пользователь не найден
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
