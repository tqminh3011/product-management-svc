package com.smg.backend.productmanagement.service;

import com.smg.backend.productmanagement.controller.model.CreateProductRequest;
import com.smg.backend.productmanagement.domain.Product;
import com.smg.backend.productmanagement.domain.ProductOwner;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> createProduct(CreateProductRequest request, ProductOwner owner);
    Mono<ProductOwner> getProductOwner(String name);
}
