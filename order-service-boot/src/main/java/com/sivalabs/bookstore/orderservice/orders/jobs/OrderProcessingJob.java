package com.sivalabs.bookstore.orderservice.orders.jobs;

import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.Order;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.Address;
import com.sivalabs.bookstore.orderservice.orders.domain.model.Customer;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderItem;
import com.sivalabs.bookstore.orderservice.orders.events.OrderEventPublisher;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingJob.class);
    private final OrderService orderService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderProcessingJob(OrderService orderService, OrderEventPublisher orderEventPublisher) {
        this.orderService = orderService;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Scheduled(fixedDelay = 10000)
    public void processNewOrders() {
        List<Order> newOrders = orderService.findOrdersByStatus(OrderStatus.NEW);
        for (Order order : newOrders) {
            OrderCreatedEvent orderCreatedEvent = this.buildOrderCreatedEvent(order);
            orderEventPublisher.publish(orderCreatedEvent);
            log.info("Published OrderCreatedEvent for orderId:{}", order.getOrderId());
            orderService.updateOrderStatus(order.getOrderId(), OrderStatus.IN_PROCESS, null);
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(
                order.getOrderId(),
                getOrderItems(order),
                getCustomer(order),
                getDeliveryAddress(order));
    }

    private Set<OrderItem> getOrderItems(Order order) {
        return order.getItems().stream()
                .map(
                        item ->
                                new OrderItem(
                                        item.getCode(),
                                        item.getName(),
                                        item.getPrice(),
                                        item.getQuantity()))
                .collect(Collectors.toSet());
    }

    private Customer getCustomer(Order order) {
        return new Customer(
                order.getCustomerName(), order.getCustomerEmail(), order.getCustomerPhone());
    }

    private Address getDeliveryAddress(Order order) {
        return new Address(
                order.getDeliveryAddressLine1(),
                order.getDeliveryAddressLine2(),
                order.getDeliveryAddressCity(),
                order.getDeliveryAddressState(),
                order.getDeliveryAddressZipCode(),
                order.getDeliveryAddressCountry());
    }
}
