package de.fhdo.spring.user.context.domain;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Provider extends User {

	private String companyName;

    public Provider(Password password, Email email, Address address, String phoneNumber, String companyName, PaymentInfo paymentInfo) {
        super();
        //super(password, email, address, phoneNumber, paymentInfo);
        this.companyName = companyName;
    }

    // Getter
    public String getCompanyName() {
        return companyName;
    }

    // Setter
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
