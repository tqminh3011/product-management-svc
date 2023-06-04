package com.smg.backend.productmanagement.service;


import com.smg.backend.productmanagement.core.enums.ProductActivity;

public interface ProductTrackingService {
    void asyncTrackProductSuccess(String productId, String productOwnerId, ProductActivity action);

    void asyncTrackProductFailure(String productOwnerId, ProductActivity action, Throwable throwable);
}
