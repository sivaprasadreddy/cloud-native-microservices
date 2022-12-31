package com.sivalabs.bookstore.orderservice.orders.events;

import static com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus.CANCELLED;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.sivalabs.bookstore.orderservice.ApplicationProperties;
import com.sivalabs.bookstore.orderservice.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orderservice.orders.domain.OrderRepository;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.Order;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCancelledEvent;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderCancelledEventHandlerTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(OrderCancelledEventHandlerTest.class);

    @Autowired private OrderRepository orderRepository;

    @Autowired private OrderEventPublisher orderEventPublisher;

    @Autowired private ApplicationProperties properties;

    @Test
    void shouldHandleOrderCancelledEvent() {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.NEW);
        order.setCustomerName("Siva");
        order.setCustomerEmail("siva@gmail.com");
        order.setDeliveryAddressLine1("addr line 1");
        order.setDeliveryAddressLine2("addr line 2");
        order.setDeliveryAddressCity("Hyderabad");
        order.setDeliveryAddressState("Telangana");
        order.setDeliveryAddressZipCode("500072");
        order.setDeliveryAddressCountry("India");

        orderRepository.saveAndFlush(order);
        log.info("Cancelling OrderId:{}", order.getOrderId());
        orderEventPublisher.publish(
                new OrderCancelledEvent(order.getOrderId(), "testing", Set.of(), null, null));

        await().atMost(30, SECONDS)
                .untilAsserted(
                        () -> {
                            Optional<Order> orderOptional =
                                    orderRepository.findByOrderId(order.getOrderId());
                            assertThat(orderOptional).isPresent();
                            assertThat(orderOptional.get().getStatus()).isEqualTo(CANCELLED);
                        });
    }
}
