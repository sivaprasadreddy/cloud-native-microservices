package com.sivalabs.bookstore.orderservice.orders.domain.model;

import java.io.Serializable;
import java.util.Set;

public record OrderCancelledEvent(
        String orderId,
        String reason,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress)
        implements Serializable {}
