package com.smg.backend.productmanagement.controller;

import com.smg.backend.productmanagement.controller.model.CreateProductRequest;
import com.smg.backend.productmanagement.domain.Product;
import com.smg.backend.productmanagement.usecase.CreateProductUseCase;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductManagementController {

    private final CreateProductUseCase createProductUseCase;

    public ProductManagementController(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = createProductUseCase;
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody @Valid CreateProductRequest request) {
        return createProductUseCase.execute(request);
    }

}
