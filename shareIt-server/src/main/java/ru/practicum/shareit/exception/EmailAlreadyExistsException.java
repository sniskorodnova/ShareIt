package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если емейл пользователя уже существует
 */
public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
