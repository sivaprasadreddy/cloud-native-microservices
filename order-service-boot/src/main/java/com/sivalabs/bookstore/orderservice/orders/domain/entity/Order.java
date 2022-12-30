package com.sivalabs.bookstore.orderservice.orders.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private Set<OrderItem> items;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String deliveryAddressLine1;

    private String deliveryAddressLine2;

    @Column(nullable = false)
    private String deliveryAddressCity;

    @Column(nullable = false)
    private String deliveryAddressState;

    @Column(nullable = false)
    private String deliveryAddressZipCode;

    @Column(nullable = false)
    private String deliveryAddressCountry;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String comments;

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0.0");
        for (OrderItem orderItem : items) {
            amount = amount.add(orderItem.getSubTotal());
        }
        return amount;
    }

    public Long getId() {
        return this.id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public Set<OrderItem> getItems() {
        return this.items;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public String getCustomerPhone() {
        return this.customerPhone;
    }

    public String getDeliveryAddressLine1() {
        return this.deliveryAddressLine1;
    }

    public String getDeliveryAddressLine2() {
        return this.deliveryAddressLine2;
    }

    public String getDeliveryAddressCity() {
        return this.deliveryAddressCity;
    }

    public String getDeliveryAddressState() {
        return this.deliveryAddressState;
    }

    public String getDeliveryAddressZipCode() {
        return this.deliveryAddressZipCode;
    }

    public String getDeliveryAddressCountry() {
        return this.deliveryAddressCountry;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public String getComments() {
        return this.comments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setDeliveryAddressLine1(String deliveryAddressLine1) {
        this.deliveryAddressLine1 = deliveryAddressLine1;
    }

    public void setDeliveryAddressLine2(String deliveryAddressLine2) {
        this.deliveryAddressLine2 = deliveryAddressLine2;
    }

    public void setDeliveryAddressCity(String deliveryAddressCity) {
        this.deliveryAddressCity = deliveryAddressCity;
    }

    public void setDeliveryAddressState(String deliveryAddressState) {
        this.deliveryAddressState = deliveryAddressState;
    }

    public void setDeliveryAddressZipCode(String deliveryAddressZipCode) {
        this.deliveryAddressZipCode = deliveryAddressZipCode;
    }

    public void setDeliveryAddressCountry(String deliveryAddressCountry) {
        this.deliveryAddressCountry = deliveryAddressCountry;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
