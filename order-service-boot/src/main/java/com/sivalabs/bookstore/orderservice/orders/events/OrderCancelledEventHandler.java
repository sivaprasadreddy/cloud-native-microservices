package com.sivalabs.bookstore.orderservice.orders.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCancelledEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelledEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderCancelledEventHandler.class);

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderCancelledEventHandler(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.cancelled-orders-topic}", groupId = "orders")
    public void handle(String payload) {
        try {
            OrderCancelledEvent event = objectMapper.readValue(payload, OrderCancelledEvent.class);
            log.info("Received a OrderCancelledEvent with orderId:{}: ", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderCancelledEvent with orderId:{}: ", event.orderId());
                return;
            }
            orderService.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED, event.reason());
        } catch (JsonProcessingException e) {
            log.error("Error processing OrderCancelledEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
