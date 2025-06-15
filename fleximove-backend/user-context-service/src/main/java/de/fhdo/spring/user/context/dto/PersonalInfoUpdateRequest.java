package de.fhdo.spring.user.context.dto;

import de.fhdo.spring.user.context.domain.DriverLicenseType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalInfoUpdateRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private DriverLicenseType driverLicenseType;
}

