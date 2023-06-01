package com.smg.backend.productmanagement.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamOperations;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventProducerImpl implements EventProducer {

    private final StreamOperations streamOps;

    public EventProducerImpl(StreamOperations streamOps) {
        this.streamOps = streamOps;
    }

    @Override
    public void publishMessage(String bindingName, Object payload) {
        streamOps.send(bindingName, payload);
    }
}
