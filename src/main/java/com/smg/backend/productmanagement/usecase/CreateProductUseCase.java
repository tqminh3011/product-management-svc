package com.smg.backend.productmanagement.usecase;

import com.smg.backend.productmanagement.controller.model.CreateProductRequest;
import com.smg.backend.productmanagement.domain.Product;
import reactor.core.publisher.Mono;

public interface CreateProductUseCase {
    Mono<Product> execute(CreateProductRequest createProductRequest);
}
