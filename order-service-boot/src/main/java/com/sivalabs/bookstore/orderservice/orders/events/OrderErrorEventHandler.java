package com.sivalabs.bookstore.orderservice.orders.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderErrorEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderErrorEventHandler.class);
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderErrorEventHandler(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.error-orders-topic}", groupId = "orders")
    public void handle(String payload) {
        try {
            OrderErrorEvent event = objectMapper.readValue(payload, OrderErrorEvent.class);
            log.info("Received a OrderErrorEvent with orderId:{}", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderErrorEvent with orderId:{}", event.orderId());
                return;
            }
            orderService.updateOrderStatus(event.orderId(), OrderStatus.ERROR, event.reason());
        } catch (JsonProcessingException e) {
            log.error("Error processing OrderErrorEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
