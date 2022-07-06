package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение для валидаций данных
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
