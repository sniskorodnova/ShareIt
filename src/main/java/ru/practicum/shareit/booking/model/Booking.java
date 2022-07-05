package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
public class Booking {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private Long booker;
    private Status status;
}
