package com.smg.backend.productmanagement.messaging;

public interface EventProducer {
    public void publishMessage(String bindingName, Object payload);
}
