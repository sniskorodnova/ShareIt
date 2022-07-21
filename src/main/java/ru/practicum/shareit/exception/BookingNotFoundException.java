package ru.practicum.shareit.exception;

/**
 * Класс, описывающий исключение, если запрашиваемое бронирование не найдено
 */
public class BookingNotFoundException extends Exception {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
