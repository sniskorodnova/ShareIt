package ru.practicum.shareit.json.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemCreateDto;


import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemCreateDtoJsonTest {
    @Autowired
    private JacksonTester<ItemCreateDto> json;

    @Test
    void TestBookingCreateDto() throws Exception {
        ItemCreateDto itemCreateDto = new ItemCreateDto(
                "Item",
                "Description",
                true,
                1L
        );
        JsonContent<ItemCreateDto> result = json.write(itemCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}
