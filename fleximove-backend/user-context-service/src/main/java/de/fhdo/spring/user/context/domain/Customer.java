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

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private DriverLicenseType driverLicenseType;

    public Customer(String firstName, String lastName, Password password, Email email, LocalDate dateOfBirth,
            Address address, DriverLicenseType driverLicenseType, String phoneNumber, PaymentInfo paymentInfo) {
    	super(password, email, address, phoneNumber,paymentInfo);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.driverLicenseType = driverLicenseType;
    }
}