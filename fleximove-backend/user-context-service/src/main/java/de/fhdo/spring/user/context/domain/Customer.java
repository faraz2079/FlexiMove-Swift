package de.fhdo.spring.user.context.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Customer extends User {

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private DriverLicenseType driverLicenseType;

    public Customer(Password password, Email email, LocalDate dateOfBirth,
            Address address, DriverLicenseType driverLicenseType, String phoneNumber, PaymentInfo paymentInfo) {
        super(password, email, address, phoneNumber,paymentInfo);
        this.dateOfBirth = dateOfBirth;
        this.driverLicenseType = driverLicenseType;
    }
}