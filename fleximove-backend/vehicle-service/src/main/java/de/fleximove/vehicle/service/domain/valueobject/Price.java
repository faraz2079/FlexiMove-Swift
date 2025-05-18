package de.fleximove.vehicle.service.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Price {
    private double amount;
    @Enumerated(EnumType.STRING)
    private BillingModel billingModel;

    public Price(double amount, BillingModel billingModel) {
        if (amount < 0) {
            throw new IllegalArgumentException("Price amount must be positive");
        }
        this.amount = amount;
        this.billingModel = billingModel;
    }

    public Price() {

    }

    public double getAmount() {
        return amount;
    }

    public BillingModel getBillingModel() {
        return billingModel;
    }
}
