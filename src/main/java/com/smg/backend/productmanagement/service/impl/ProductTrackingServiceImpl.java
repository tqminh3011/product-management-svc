package com.smg.backend.productmanagement.service.impl;

import com.smg.backend.productmanagement.enums.ProductActivity;
import com.smg.backend.productmanagement.repository.ProductTrackingRepository;
import com.smg.backend.productmanagement.repository.entity.ProductTrackingEntity;
import com.smg.backend.productmanagement.service.ProductTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductTrackingServiceImpl implements ProductTrackingService {

    private final ProductTrackingRepository repository;

    public ProductTrackingServiceImpl(ProductTrackingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void asyncTrackProductSuccess(String productId, String productOwnerId, ProductActivity action) {
        trackProduct(productId, productOwnerId, action, ProductTrackingEntity.Status.SUCCESS, null);
    }

    @Override
    public void asyncTrackProductFailure(String productOwnerId, ProductActivity action, Throwable throwable) {
        trackProduct(null, productOwnerId, action, ProductTrackingEntity.Status.FAILED, throwable.toString());
    }

    private void trackProduct(String productId,
                              String productOwnerId,
                              ProductActivity action,
                              ProductTrackingEntity.Status status,
                              String metadata) {
        log.info("Tracking product activity: productId={}, ownerId={}, action={}, status={}, metadata={} ",
                productId,
                productOwnerId,
                action,
                status,
                metadata);

        repository.save(
                ProductTrackingEntity.builder()
                        .activity(action)
                        .productId(productId)
                        .productOwnerId(productOwnerId)
                        .status(status)
                        .metadata(metadata)
                        .build()
        ).subscribe()
                ;
    }


}
