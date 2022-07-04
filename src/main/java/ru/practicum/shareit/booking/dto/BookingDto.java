package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDate;

public class BookingDto {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private Status status;

    static private class User {
        private Long id;
        private String name;
    }

    static private class Item {
        private Long id;
        private String name;
        private String description;
        private boolean available;
    }
}
