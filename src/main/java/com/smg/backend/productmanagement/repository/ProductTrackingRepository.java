package com.smg.backend.productmanagement.repository;

import com.smg.backend.productmanagement.repository.entity.ProductTrackingEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTrackingRepository extends ReactiveCrudRepository<ProductTrackingEntity, Long> {
}
