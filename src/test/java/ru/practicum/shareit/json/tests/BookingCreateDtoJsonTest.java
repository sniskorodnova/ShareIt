package ru.practicum.shareit.json.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingCreateDtoJsonTest {
    @Autowired
    private JacksonTester<BookingCreateDto> json;

    @Test
    void testBookingCreateDto() throws Exception {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.of(2022, 07, 22, 07, 35),
                LocalDateTime.of(2022, 07, 25, 10, 00),
                1L
        );
        JsonContent<BookingCreateDto> result = json.write(bookingCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-07-22T07:35:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-07-25T10:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}
