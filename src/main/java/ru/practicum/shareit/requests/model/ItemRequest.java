package ru.practicum.shareit.requests.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private long requesterId;
    private LocalDateTime created;
}
