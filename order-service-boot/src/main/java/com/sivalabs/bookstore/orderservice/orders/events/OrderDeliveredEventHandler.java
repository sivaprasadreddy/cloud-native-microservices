package com.sivalabs.bookstore.orderservice.orders.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDeliveredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveredEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderDeliveredEventHandler.class);
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderDeliveredEventHandler(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.delivered-orders-topic}", groupId = "orders")
    public void handle(String payload) {
        try {
            OrderDeliveredEvent event = objectMapper.readValue(payload, OrderDeliveredEvent.class);
            log.info("Received a OrderDeliveredEvent with orderId:{}: ", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderDeliveredEvent with orderId:{}: ", event.orderId());
                return;
            }
            orderService.updateOrderStatus(order.orderId(), OrderStatus.DELIVERED, null);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error processing OrderDeliveredEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
