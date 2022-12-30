package com.sivalabs.delivery;

import com.sivalabs.delivery.model.OrderCancelledEvent;
import com.sivalabs.delivery.model.OrderCreatedEvent;
import com.sivalabs.delivery.model.OrderDeliveredEvent;
import com.sivalabs.delivery.model.OrderErrorEvent;
import jakarta.inject.Singleton;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES =
            List.of("INDIA", "USA", "GERMANY", "UK");

    private final KafkaMessageClient kafkaMessageClient;
    private final ApplicationProperties properties;

    public DeliveryService(
            KafkaMessageClient kafkaMessageClient, ApplicationProperties properties) {
        this.kafkaMessageClient = kafkaMessageClient;
        this.properties = properties;
    }

    public void process(OrderCreatedEvent event) {
        try {
            if (canBeDelivered(event)) {
                logger.info("OrderId: {} can be delivered", event.orderId());
                kafkaMessageClient.send(
                        properties.getDeliveredOrdersTopic(), buildOrderDeliveredEvent(event));
                logger.info("Published OrderDelivered event with OrderId: {}", event.orderId());
            } else {
                logger.info("OrderId: {} can not be delivered", event.orderId());
                kafkaMessageClient.send(
                        properties.getCancelledOrdersTopic(),
                        buildOrderCancelledEvent(event, "Can't deliver to the location"));
                logger.info("Published OrderCancelled event with OrderId: {}", event.orderId());
            }
        } catch (RuntimeException e) {
            logger.error("Failed to process OrderCreatedEvent with orderId: " + event.orderId(), e);
            kafkaMessageClient.send(
                    properties.getErrorOrdersTopic(), buildOrderErrorEvent(event, e.getMessage()));
            logger.info("Published OrderError event with OrderId: {}", event.orderId());
        }
    }

    private boolean canBeDelivered(OrderCreatedEvent order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(order.deliveryAddress().country().toUpperCase());
    }

    private OrderDeliveredEvent buildOrderDeliveredEvent(OrderCreatedEvent order) {
        return new OrderDeliveredEvent(
                order.orderId(), order.items(), order.customer(), order.deliveryAddress());
    }

    private OrderCancelledEvent buildOrderCancelledEvent(OrderCreatedEvent order, String reason) {
        return new OrderCancelledEvent(
                order.orderId(), order.items(), order.customer(), order.deliveryAddress(), reason);
    }

    private OrderErrorEvent buildOrderErrorEvent(OrderCreatedEvent order, String reason) {
        return new OrderErrorEvent(
                order.orderId(), order.items(), order.customer(), order.deliveryAddress(), reason);
    }
}