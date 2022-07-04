package ru.practicum.shareit.requests.dto;

import java.time.LocalDateTime;

public class ItemRequestDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;

    static private class User {
        private Long id;
        private String name;
    }
}
