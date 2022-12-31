package com.sivalabs.bookstore.delivery;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("app")
public class ApplicationProperties {
    private String newOrdersTopic;
    private String deliveredOrdersTopic;
    private String cancelledOrdersTopic;
    private String errorOrdersTopic;

    public String getNewOrdersTopic() {
        return newOrdersTopic;
    }

    public void setNewOrdersTopic(String newOrdersTopic) {
        this.newOrdersTopic = newOrdersTopic;
    }

    public String getDeliveredOrdersTopic() {
        return deliveredOrdersTopic;
    }

    public void setDeliveredOrdersTopic(String deliveredOrdersTopic) {
        this.deliveredOrdersTopic = deliveredOrdersTopic;
    }

    public String getCancelledOrdersTopic() {
        return cancelledOrdersTopic;
    }

    public void setCancelledOrdersTopic(String cancelledOrdersTopic) {
        this.cancelledOrdersTopic = cancelledOrdersTopic;
    }

    public String getErrorOrdersTopic() {
        return errorOrdersTopic;
    }

    public void setErrorOrdersTopic(String errorOrdersTopic) {
        this.errorOrdersTopic = errorOrdersTopic;
    }
}
