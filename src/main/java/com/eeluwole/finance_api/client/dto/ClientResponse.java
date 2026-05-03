package com.eeluwole.finance_api.client.dto;

import com.eeluwole.finance_api.client.Client.ClientStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private ClientStatus status;
    private LocalDateTime createdAt;
}