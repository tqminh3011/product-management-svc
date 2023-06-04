package com.smg.backend.productmanagement.service;

import com.smg.backend.productmanagement.core.enums.ProductActivity;
import com.smg.backend.productmanagement.core.model.CreateProductRequest;
import com.smg.backend.productmanagement.core.model.Product;
import com.smg.backend.productmanagement.core.model.ProductOwner;
import com.smg.backend.productmanagement.core.port.ProductService;
import com.smg.backend.productmanagement.messaging.Event;
import com.smg.backend.productmanagement.messaging.EventProducer;
import com.smg.backend.productmanagement.repository.ProductOwnerRepository;
import com.smg.backend.productmanagement.repository.ProductRepository;
import com.smg.backend.productmanagement.repository.entity.ProductEntity;
import com.smg.backend.productmanagement.repository.entity.ProductOwnerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_EVENTS_CHANNEL_OUT = "product-events-channel-out-0";

    private final ProductRepository productRepository;
    private final ProductOwnerRepository productOwnerRepository;
    private final ProductTrackingService productTrackingService;
    private final EventProducer eventProducer;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductOwnerRepository productOwnerRepository,
                              ProductTrackingService productTrackingService,
                              EventProducer eventProducer) {
        this.productRepository = productRepository;
        this.productOwnerRepository = productOwnerRepository;
        this.eventProducer = eventProducer;
        this.productTrackingService = productTrackingService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Product> createProduct(CreateProductRequest request, ProductOwner owner) {
        return productRepository.save(
                        ProductEntity.builder()
                                .productOwnerId(Long.valueOf(owner.id()))
                                .externalId(UUID.randomUUID().toString())
                                .name(request.name())
                                .type(request.type())
                                .price(request.price())
                                .build()
                )
                .map(entity -> toProduct(entity, owner.name()))
                .doOnNext(this::publishProductCreated)
                .doOnSuccess(product ->
                        productTrackingService.asyncTrackProductSuccess(product.id(), owner.id(), ProductActivity.CREATE)
                )
                .doOnError(error ->
                        productTrackingService.asyncTrackProductFailure(owner.id(), ProductActivity.CREATE, error)
                );
    }

    @Override
    public Mono<ProductOwner> getProductOwner(String name) {
        return productOwnerRepository.findByName(name)
                .map(this::toProductOwner);
    }

    private Product toProduct(ProductEntity entity, String ownerName) {
        return new Product(
                entity.getExternalId(),
                entity.getName(),
                ownerName,
                entity.getType(),
                entity.getPrice(),
                entity.getCreatedDate(),
                entity.getUpdatedDate()
        );
    }

    private void publishProductCreated(Product product) {
        Message<Event<Object>> payload = MessageBuilder
                .withPayload(Event.builder()
                        .id(product.generateContentHash())
                        .source("product-management-svc")
                        .detail(product)
                        .time(System.currentTimeMillis())
                        .version("1")
                        .build())
                .build();
        log.info("Publishing created product event, payload={}", payload);

        eventProducer.publishMessage(
                PRODUCT_EVENTS_CHANNEL_OUT,
                payload
        );
    }

    private ProductOwner toProductOwner(ProductOwnerEntity entity) {
        return new ProductOwner(
                entity.getId().toString(),
                entity.getName()
        );
    }
}
