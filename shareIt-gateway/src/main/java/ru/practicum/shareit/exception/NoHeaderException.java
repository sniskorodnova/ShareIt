package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если в хедере не передан пользователь
 */
public class NoHeaderException extends Exception {
    public NoHeaderException(String message) {
        super(message);
    }
}