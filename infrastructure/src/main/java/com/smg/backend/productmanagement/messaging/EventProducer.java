package com.smg.backend.productmanagement.messaging;

public interface EventProducer {
    void publishMessage(String bindingName, Object payload);
}
