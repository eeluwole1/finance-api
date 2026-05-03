package com.eeluwole.finance_api.client.dto;

import lombok.Data;

@Data
public class CreateClientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}