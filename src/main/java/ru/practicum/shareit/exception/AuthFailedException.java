package ru.practicum.shareit.exception;

public class AuthFailedException extends Exception {
    public AuthFailedException(String message) {
        super(message);
    }
}
