package com.sivalabs.bookstore.orderservice.orders.domain.model;

import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;

public class OrderConfirmationDTO {
    private String orderId;
    private OrderStatus status;

    public OrderConfirmationDTO(String orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

    public OrderConfirmationDTO() {}

    public String getOrderId() {
        return this.orderId;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
