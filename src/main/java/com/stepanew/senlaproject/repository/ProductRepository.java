package com.stepanew.senlaproject.repository;

import com.stepanew.senlaproject.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    Page<Product> findAllByNameContainingIgnoreCaseAndCategory_Id(
            String name,
            Long categoryId,
            Pageable pageable
    );

}
