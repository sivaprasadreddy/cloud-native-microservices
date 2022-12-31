package com.sivalabs.bookstore.orderservice.orders.events;

import static com.sivalabs.bookstore.orderservice.config.RabbitMQConfig.ORDER_CANCELLED_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.orderservice.config.RabbitMQConfig.ORDER_CREATED_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.orderservice.config.RabbitMQConfig.ORDER_DELIVERED_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.orderservice.config.RabbitMQConfig.ORDER_ERROR_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.orderservice.config.RabbitMQConfig.ORDER_EVENTS_EXCHANGE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCancelledEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(OrderCreatedEvent event) {
        send(ORDER_CREATED_EVENT_ROUTING_KEY, event);
    }

    public void publish(OrderDeliveredEvent event) {
        send(ORDER_DELIVERED_EVENT_ROUTING_KEY, event);
    }

    public void publish(OrderCancelledEvent event) {
        send(ORDER_CANCELLED_EVENT_ROUTING_KEY, event);
    }

    public void publish(OrderErrorEvent event) {
        send(ORDER_ERROR_EVENT_ROUTING_KEY, event);
    }

    private void send(String routingKey, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            logger.info("Sending message to routingKey: {}, payload:{}", routingKey, json);
            rabbitTemplate.convertAndSend(ORDER_EVENTS_EXCHANGE, routingKey, payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
