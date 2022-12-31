package com.sivalabs.bookstore.delivery;

import static com.sivalabs.bookstore.delivery.RabbitMQConfig.ORDER_CREATED_EVENTS_QUEUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.delivery.model.OrderCreatedEvent;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RabbitListener
public class OrderEventListener {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);
    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;

    public OrderEventListener(DeliveryService deliveryService, ObjectMapper objectMapper) {
        this.deliveryService = deliveryService;
        this.objectMapper = objectMapper;
    }

    @Queue(ORDER_CREATED_EVENTS_QUEUE)
    public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
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
