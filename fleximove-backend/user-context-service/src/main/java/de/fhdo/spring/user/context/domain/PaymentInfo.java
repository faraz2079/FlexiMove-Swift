package de.fhdo.spring.user.context.domain;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class PaymentInfo {
    private  String creditCardNumber;
    private  String cardHolderName;
    private  String expiryDate;  // z.B. "12/25"
    private  String cvv;

    public PaymentInfo(String creditCardNumber, String cardHolderName, String expiryDate, String cvv) {
        if (creditCardNumber == null || creditCardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Kreditkartennummer darf nicht leer sein");
        }
        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Karteninhaber darf nicht leer sein");
        }
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Ablaufdatum darf nicht leer sein");
        }
        if (cvv == null || cvv.trim().isEmpty()) {
            throw new IllegalArgumentException("CVV darf nicht leer sein");
        }
        this.creditCardNumber = creditCardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentInfo)) return false;
        PaymentInfo that = (PaymentInfo) o;
        return creditCardNumber.equals(that.creditCardNumber) &&
               cardHolderName.equals(that.cardHolderName) &&
               expiryDate.equals(that.expiryDate) &&
               cvv.equals(that.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditCardNumber, cardHolderName, expiryDate, cvv);
    }

    @Override
    public String toString() {
        return "PaymentInfo{****}";
    }
}
