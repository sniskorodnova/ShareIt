package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker(Long userId);
    List<Booking> findByBookerAndEndBefore(Long userId, LocalDateTime end);

    List<Booking> findByBookerAndStartAfter(Long userId, LocalDateTime start);

    List<Booking> findByBookerAndStatus(Long userId, Status status);

    List<Booking> findByBookerAndStartBeforeAndEndBefore(Long bookerId, LocalDateTime start,
                                                                                LocalDateTime end);
}