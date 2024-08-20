package com.stepanew.senlaproject.repository;

import com.stepanew.senlaproject.domain.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Page<Store> findAllByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
            String name,
            String address,
            Pageable pageable
    );

}
