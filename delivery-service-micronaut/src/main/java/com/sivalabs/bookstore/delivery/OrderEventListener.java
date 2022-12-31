package com.sivalabs.bookstore.delivery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.delivery.model.OrderCreatedEvent;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener(offsetReset = OffsetReset.LATEST)
public class OrderEventListener {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);
    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;

    public OrderEventListener(DeliveryService deliveryService, ObjectMapper objectMapper) {
        this.deliveryService = deliveryService;
        this.objectMapper = objectMapper;
    }

    @Topic("${app.newOrdersTopic}")
    public void receive(OrderCreatedEvent orderCreatedEvent) {
        try {
            logger.info(
                    "Got OrderCreatedEvent payload: {}",
                    objectMapper.writeValueAsString(orderCreatedEvent));
            deliveryService.process(orderCreatedEvent);
        } catch (JsonProcessingException e) {
            // throw new RuntimeException(e);
            logger.error("Error parsing OrderCreatedEvent payload", e);
        }
    }
}
