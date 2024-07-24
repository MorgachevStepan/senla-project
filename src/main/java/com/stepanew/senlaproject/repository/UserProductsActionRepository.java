package com.stepanew.senlaproject.repository;

import com.stepanew.senlaproject.domain.entity.UserProductsAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProductsActionRepository extends JpaRepository<UserProductsAction, Long> {
}
