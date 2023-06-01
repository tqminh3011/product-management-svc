package com.smg.backend.productmanagement.repository;

import com.smg.backend.productmanagement.repository.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
}
