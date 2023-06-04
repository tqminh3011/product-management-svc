package com.smg.backend.productmanagement.core.usecase;

import com.smg.backend.productmanagement.core.exception.DomainErrorCode;
import com.smg.backend.productmanagement.core.exception.ProductDomainException;
import com.smg.backend.productmanagement.core.model.CreateProductRequest;
import com.smg.backend.productmanagement.core.port.ProductService;
import com.smg.backend.productmanagement.core.model.Product;
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
