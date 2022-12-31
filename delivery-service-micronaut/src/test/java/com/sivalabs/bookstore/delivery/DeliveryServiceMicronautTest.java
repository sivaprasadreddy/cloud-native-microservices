package com.sivalabs.bookstore.delivery;

import com.sivalabs.bookstore.delivery.model.Address;
import com.sivalabs.bookstore.delivery.model.Customer;
import com.sivalabs.bookstore.delivery.model.OrderCreatedEvent;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@MicronautTest
class DeliveryServiceMicronautTest {

    @Inject ApplicationProperties properties;
    @Inject OrderEventPublisher orderEventPublisher;

    @Test
    void shouldBeAbleToSendNewOrderEventMessage() {
        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        UUID.randomUUID().toString(),
                        Set.of(),
                        new Customer("Siva", "siva@gmail.com", "999999999999"),
                        new Address(
                                "addr line 1",
                                "addr line 2",
                                "Hyderabad",
                                "TS",
                                "500072",
                                "India"));
        System.out.println("Sending OrderCreatedEvent to event queue...");
        orderEventPublisher.send(event);
        // Use awaitility and assert
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Received OrderCreatedEvent?");
    }
}
