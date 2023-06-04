package com.smg.backend.productmanagement.core.usecase;

import com.smg.backend.productmanagement.core.model.CreateProductRequest;
import com.smg.backend.productmanagement.core.model.Product;
import reactor.core.publisher.Mono;

public interface CreateProductUseCase {
    Mono<Product> execute(CreateProductRequest createProductRequest);
}
