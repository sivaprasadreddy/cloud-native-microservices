package com.sivalabs.delivery.model;

import java.io.Serializable;
import java.util.Set;

public record OrderCreatedEvent(
        String orderId, Set<OrderItem> items, Customer customer, Address deliveryAddress)
        implements Serializable {}
