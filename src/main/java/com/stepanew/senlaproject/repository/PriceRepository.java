package com.stepanew.senlaproject.repository;

import com.stepanew.senlaproject.domain.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
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

}
