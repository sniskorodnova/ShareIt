package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если переданный пользователем статус не найден
 */
public class UnsupportedStatusException extends Exception {
    public UnsupportedStatusException(String message) {
        super(message);
    }
}
