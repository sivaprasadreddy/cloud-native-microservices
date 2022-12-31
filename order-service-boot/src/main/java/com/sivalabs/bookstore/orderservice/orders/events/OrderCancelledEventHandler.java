package com.sivalabs.bookstore.orderservice.orders.events;

import static com.sivalabs.bookstore.orderservice.config.RabbitMQConfig.ORDER_CANCELLED_EVENTS_QUEUE;

import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCancelledEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelledEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderCancelledEventHandler.class);

    private final OrderService orderService;

    public OrderCancelledEventHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = ORDER_CANCELLED_EVENTS_QUEUE)
    public void handle(OrderCancelledEvent event) {
        try {
            log.info("Received a OrderCancelledEvent with orderId:{}: ", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderCancelledEvent with orderId:{}: ", event.orderId());
                return;
            }
            orderService.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED, event.reason());
        } catch (RuntimeException e) {
            log.error("Error processing OrderCancelledEvent. Payload: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
