package com.sivalabs.bookstore.orderservice.orders.domain.model;

import com.sivalabs.bookstore.orderservice.orders.domain.entity.Order;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderDTO(
        Long id,
        String orderId,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        OrderStatus status,
        String comments) {

    public static OrderDTO from(Order order) {
        Set<OrderItem> orderItems =
                order.getItems().stream()
                        .map(
                                item ->
                                        new OrderItem(
                                                item.getCode(),
                                                item.getName(),
                                                item.getPrice(),
                                                item.getQuantity()))
                        .collect(Collectors.toSet());

        return new OrderDTO(
                order.getId(),
                order.getOrderId(),
                orderItems,
                new Customer(
                        order.getCustomerName(),
                        order.getCustomerEmail(),
                        order.getCustomerPhone()),
                new Address(
                        order.getDeliveryAddressLine1(),
                        order.getDeliveryAddressLine2(),
                        order.getDeliveryAddressCity(),
                        order.getDeliveryAddressState(),
                        order.getDeliveryAddressZipCode(),
                        order.getDeliveryAddressCountry()),
                order.getStatus(),
                order.getComments());
    }
}
