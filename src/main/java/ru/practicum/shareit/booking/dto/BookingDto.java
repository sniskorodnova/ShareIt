package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDate;

public class BookingDto {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private Long booker;
    private Status status;

    static class Item {
        private Long id;
        private String name;
        private String description;
        private boolean available;
    }
}
