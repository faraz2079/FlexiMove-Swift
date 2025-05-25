package de.fhdo.spring.user.context.domain;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Customer extends User {

    private String username;

    public Customer() {
        super();
    }

    public Customer(Password password, Email email, LocalDate dateOfBirth,
            Adress address, Boolean hasDrivingLicense, String phoneNumber, PaymentInfo paymentInfo) {
        super(password, email, dateOfBirth, address, hasDrivingLicense, phoneNumber,paymentInfo);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}