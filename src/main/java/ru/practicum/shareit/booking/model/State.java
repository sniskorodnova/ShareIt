package ru.practicum.shareit.booking.model;

/**
 * Класс-перечисление для возможных статусов бронирований, запрашиваемых клиентом
 */
public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED
}
