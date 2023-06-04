package com.smg.backend.productmanagement.repository;

import com.smg.backend.productmanagement.repository.entity.ProductOwnerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductOwnerRepository extends ReactiveCrudRepository<ProductOwnerEntity, Long> {
    Mono<ProductOwnerEntity> findByName(String name);
}
