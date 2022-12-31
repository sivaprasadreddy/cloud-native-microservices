package com.sivalabs.bookstore.delivery;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;
import java.io.IOException;

@Singleton
public class RabbitMQConfig extends ChannelInitializer {
    public static final String ORDER_EVENTS_EXCHANGE = "OrdersExchange";

    public static final String ORDER_CREATED_EVENT_ROUTING_KEY = "product.new";
    public static final String ORDER_DELIVERED_EVENT_ROUTING_KEY = "product.delivered";
    public static final String ORDER_CANCELLED_EVENT_ROUTING_KEY = "product.cancelled";
    public static final String ORDER_ERROR_EVENT_ROUTING_KEY = "product.error";

    public static final String ORDER_CREATED_EVENTS_QUEUE = "new-orders";
    public static final String ORDER_DELIVERED_EVENTS_QUEUE = "delivered-orders";
    public static final String ORDER_CANCELLED_EVENTS_QUEUE = "cancelled-orders";
    public static final String ORDER_ERROR_EVENTS_QUEUE = "error-orders";

    @Override
    public void initialize(Channel channel, String name) throws IOException {
        channel.exchangeDeclare(ORDER_EVENTS_EXCHANGE, BuiltinExchangeType.TOPIC, true);

        channel.queueDeclare(ORDER_CREATED_EVENTS_QUEUE, false, false, false, null);
        channel.queueBind(
                ORDER_CREATED_EVENTS_QUEUE, ORDER_EVENTS_EXCHANGE, ORDER_CREATED_EVENT_ROUTING_KEY);

        channel.queueDeclare(ORDER_DELIVERED_EVENTS_QUEUE, false, false, false, null);
        channel.queueBind(
                ORDER_DELIVERED_EVENTS_QUEUE,
                ORDER_EVENTS_EXCHANGE,
                ORDER_DELIVERED_EVENT_ROUTING_KEY);

        channel.queueDeclare(ORDER_CANCELLED_EVENTS_QUEUE, false, false, false, null);
        channel.queueBind(
                ORDER_CANCELLED_EVENTS_QUEUE,
                ORDER_EVENTS_EXCHANGE,
                ORDER_CANCELLED_EVENT_ROUTING_KEY);

        channel.queueDeclare(ORDER_ERROR_EVENTS_QUEUE, false, false, false, null);
        channel.queueBind(
                ORDER_ERROR_EVENTS_QUEUE, ORDER_EVENTS_EXCHANGE, ORDER_ERROR_EVENT_ROUTING_KEY);
    }
}
