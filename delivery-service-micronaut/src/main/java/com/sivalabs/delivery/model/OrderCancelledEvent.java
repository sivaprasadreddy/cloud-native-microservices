package com.sivalabs.delivery.model;

import java.io.Serializable;
import java.util.Set;

public record OrderCancelledEvent(
        String orderId,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason)
        implements Serializable {}
