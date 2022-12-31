package com.sivalabs.bookstore.orderservice.orders.events;

import static com.sivalabs.bookstore.orderservice.config.RabbitMQConfig.ORDER_DELIVERED_EVENTS_QUEUE;

import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDeliveredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveredEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderDeliveredEventHandler.class);
    private final OrderService orderService;

    public OrderDeliveredEventHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = ORDER_DELIVERED_EVENTS_QUEUE)
    public void handle(OrderDeliveredEvent event) {
        try {
            log.info("Received a OrderDeliveredEvent with orderId:{}: ", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderDeliveredEvent with orderId:{}: ", event.orderId());
                return;
            }
            orderService.updateOrderStatus(order.orderId(), OrderStatus.DELIVERED, null);
        } catch (RuntimeException e) {
            log.error("Error processing OrderDeliveredEvent. Payload: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
