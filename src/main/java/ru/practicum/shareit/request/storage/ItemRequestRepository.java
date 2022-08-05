package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Репозиторий для работы с запросами вещей
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Long userId);

    List<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(Long userId, Pageable pageable);
}
