package com.stepanew.senlaproject.repository;

import com.stepanew.senlaproject.domain.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
            Long productId,
            Long storeId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Price> findAllByProduct_IdAndCheckedDateBetween(
            Long productId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    Optional<Price> findTopByProduct_IdAndStore_IdOrderByCheckedDateDesc(
            Long productId,
            Long storeId
    );

}
