package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если запрашивается объект, не принадлежащий пользователю, переданному
 * в хедере
 */
public class AuthFailedException extends Exception {
    public AuthFailedException(String message) {
        super(message);
    }
}
