package com.payment.service.domain.valueObject;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Amount {
    private double amountValue;

    public Amount(double amountValue) {
        if (amountValue < 0) {
            throw new IllegalArgumentException("Price amount must be positive");
        }
        this.amountValue = amountValue;
    }

    public double getAmountValue() {
        return amountValue;
    }
}
