package com.sivalabs.bookstore.delivery.model;

import java.io.Serializable;
import java.util.Set;

public record OrderDeliveredEvent(
        String orderId, Set<OrderItem> items, Customer customer, Address deliveryAddress)
        implements Serializable {}
