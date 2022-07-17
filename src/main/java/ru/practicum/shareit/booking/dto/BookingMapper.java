package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking toBookingCreate(BookingCreateDto bookingCreateDto) {
        return new Booking(
                null,
                bookingCreateDto.getStart(),
                bookingCreateDto.getEnd(),
                new Item (bookingCreateDto.getItemId(), null, null, null,
                        null, null),
                null,
                null
        );
    }
}
