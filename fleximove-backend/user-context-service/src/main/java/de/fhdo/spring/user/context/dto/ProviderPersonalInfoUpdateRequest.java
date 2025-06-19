package de.fhdo.spring.user.context.dto;

import lombok.Data;

@Data
public class ProviderPersonalInfoUpdateRequest {
    private String companyName;
    private String phoneNumber;
}

