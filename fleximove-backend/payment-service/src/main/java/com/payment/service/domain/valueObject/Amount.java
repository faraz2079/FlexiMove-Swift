package com.payment.service.domain.valueObject;

import jakarta.persistence.Embeddable;

@Embeddable
public class Amount {
    private double amountValue;

    public Amount() {}

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
