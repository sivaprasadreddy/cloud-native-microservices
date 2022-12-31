package com.sivalabs.bookstore.delivery.model;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

public record Address(
        @NotBlank(message = "AddressLine1 is required") String addressLine1,
        String addressLine2,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "State is required") String state,
        @NotBlank(message = "ZipCode is required") String zipCode,
        @NotBlank(message = "Country is required") String country)
        implements Serializable {}
