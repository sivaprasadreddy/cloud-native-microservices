package com.sivalabs.bookstore.delivery;

import static com.sivalabs.bookstore.delivery.RabbitMQConfig.ORDER_CANCELLED_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.delivery.RabbitMQConfig.ORDER_CREATED_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.delivery.RabbitMQConfig.ORDER_DELIVERED_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.delivery.RabbitMQConfig.ORDER_ERROR_EVENT_ROUTING_KEY;
import static com.sivalabs.bookstore.delivery.RabbitMQConfig.ORDER_EVENTS_EXCHANGE;

import com.sivalabs.bookstore.delivery.model.OrderCancelledEvent;
import com.sivalabs.bookstore.delivery.model.OrderCreatedEvent;
import com.sivalabs.bookstore.delivery.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.delivery.model.OrderErrorEvent;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient(ORDER_EVENTS_EXCHANGE)
public interface OrderEventPublisher {

    @Binding(ORDER_CREATED_EVENT_ROUTING_KEY)
    void send(OrderCreatedEvent event);

    @Binding(ORDER_DELIVERED_EVENT_ROUTING_KEY)
    void send(OrderDeliveredEvent event);

    @Binding(ORDER_CANCELLED_EVENT_ROUTING_KEY)
    void send(OrderCancelledEvent event);

    @Binding(ORDER_ERROR_EVENT_ROUTING_KEY)
    void send(OrderErrorEvent event);
}
