package com.es.core.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderDTO {
    @NotBlank(message = "First name is required!")
    private String firstName;
    @NotBlank(message = "Last name is required!")
    private String lastName;
    @NotBlank(message = "Delivery address is required!")
    private String deliveryAddress;
    @NotBlank(message = "Phone is required!")
    private String contactPhoneNo;
}
