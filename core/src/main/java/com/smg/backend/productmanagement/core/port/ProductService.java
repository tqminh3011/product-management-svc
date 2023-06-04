package com.smg.backend.productmanagement.core.port;

import com.smg.backend.productmanagement.core.model.ProductOwner;
import com.smg.backend.productmanagement.core.model.CreateProductRequest;
import com.smg.backend.productmanagement.core.model.Product;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> createProduct(CreateProductRequest request, ProductOwner owner);
    Mono<ProductOwner> getProductOwner(String name);
}
