package com.payment.service.domain.valueObject;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@Embeddable
public class Amount {
    private double amountValue;

    public Amount(double amountValue) {
        if (amountValue < 0) {
            throw new IllegalArgumentException("Price amount must be positive");
        }
        this.amountValue = Objects.requireNonNull(amountValue, "amount must not be null");
    }

    public double getAmountValue() {
        return amountValue;
    }
}
