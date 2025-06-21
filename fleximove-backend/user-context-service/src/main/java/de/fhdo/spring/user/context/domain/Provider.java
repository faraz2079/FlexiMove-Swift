package de.fhdo.spring.user.context.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Provider extends User {

	private String companyName;

    public Provider(Password password, Email email, Address address, String phoneNumber, String companyName, PaymentInfo paymentInfo) {
        super(password, email, address, phoneNumber, paymentInfo);
        this.companyName = companyName;
    }
}
