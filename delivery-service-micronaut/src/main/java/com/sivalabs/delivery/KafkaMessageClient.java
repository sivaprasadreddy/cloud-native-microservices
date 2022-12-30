package com.sivalabs.delivery;

import com.sivalabs.delivery.model.OrderCancelledEvent;
import com.sivalabs.delivery.model.OrderDeliveredEvent;
import com.sivalabs.delivery.model.OrderErrorEvent;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface KafkaMessageClient {

    void send(@Topic String topic, OrderDeliveredEvent event);

    void send(@Topic String topic, OrderCancelledEvent event);

    void send(@Topic String topic, OrderErrorEvent event);
}
