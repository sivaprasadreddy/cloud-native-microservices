package com.sivalabs.bookstore.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_EVENTS_EXCHANGE = "OrdersExchange";

    public static final String ORDER_CREATED_EVENT_ROUTING_KEY = "product.new";
    public static final String ORDER_DELIVERED_EVENT_ROUTING_KEY = "product.delivered";
    public static final String ORDER_CANCELLED_EVENT_ROUTING_KEY = "product.cancelled";
    public static final String ORDER_ERROR_EVENT_ROUTING_KEY = "product.error";

    public static final String ORDER_CREATED_EVENTS_QUEUE = "new-orders";
    public static final String ORDER_DELIVERED_EVENTS_QUEUE = "delivered-orders";
    public static final String ORDER_CANCELLED_EVENTS_QUEUE = "cancelled-orders";
    public static final String ORDER_ERROR_EVENTS_QUEUE = "error-orders";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE);
    }

    @Bean
    Queue newOrdersQueue() {
        return new Queue(ORDER_CREATED_EVENTS_QUEUE, false);
    }

    @Bean
    Binding newOrdersQueueBinding(Queue newOrdersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(newOrdersQueue)
                .to(exchange)
                .with(ORDER_CREATED_EVENT_ROUTING_KEY);
    }

    @Bean
    Queue deliveredOrdersQueue() {
        return new Queue(ORDER_DELIVERED_EVENTS_QUEUE, false);
    }

    @Bean
    Binding deliveredOrdersQueueBinding(Queue deliveredOrdersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deliveredOrdersQueue)
                .to(exchange)
                .with(ORDER_DELIVERED_EVENT_ROUTING_KEY);
    }

    @Bean
    Queue cancelledOrdersQueue() {
        return new Queue(ORDER_CANCELLED_EVENTS_QUEUE, false);
    }

    @Bean
    Binding cancelledOrdersQueueBinding(Queue cancelledOrdersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cancelledOrdersQueue)
                .to(exchange)
                .with(ORDER_CANCELLED_EVENT_ROUTING_KEY);
    }

    @Bean
    Queue errorOrdersQueue() {
        return new Queue(ORDER_ERROR_EVENTS_QUEUE, false);
    }

    @Bean
    Binding errorOrdersQueueBinding(Queue errorOrdersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(errorOrdersQueue)
                .to(exchange)
                .with(ORDER_ERROR_EVENT_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter(
            ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
