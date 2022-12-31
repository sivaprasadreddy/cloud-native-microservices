package com.sivalabs.bookstore.delivery;

import com.sivalabs.bookstore.delivery.model.OrderCancelledEvent;
import com.sivalabs.bookstore.delivery.model.OrderCreatedEvent;
import com.sivalabs.bookstore.delivery.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.delivery.model.OrderErrorEvent;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface KafkaMessagePublisher {

    void send(@Topic String topic, OrderCreatedEvent event);

    void send(@Topic String topic, OrderDeliveredEvent event);

    void send(@Topic String topic, OrderCancelledEvent event);

    void send(@Topic String topic, OrderErrorEvent event);
}
