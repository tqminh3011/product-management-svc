package com.smg.backend.productmanagement.usecase;

import com.smg.backend.productmanagement.controller.model.CreateProductRequest;
import com.smg.backend.productmanagement.domain.Product;
import com.smg.backend.productmanagement.exception.DomainErrorCode;
import com.smg.backend.productmanagement.exception.ProductDomainException;
import com.smg.backend.productmanagement.service.ProductService;
import reactor.core.publisher.Mono;

public class CreateProductUseCaseImpl implements CreateProductUseCase {
    private final ProductService productService;

    public CreateProductUseCaseImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Mono<Product> execute(CreateProductRequest createProductRequest) {
        return productService.getProductOwner(createProductRequest.productOwner())
                .switchIfEmpty(Mono.error(new ProductDomainException(DomainErrorCode.INVALID_OWNER)))
                .flatMap(owner -> productService.createProduct(createProductRequest, owner));
    }
}
