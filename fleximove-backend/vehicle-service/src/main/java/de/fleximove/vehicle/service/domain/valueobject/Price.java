package de.fleximove.vehicle.service.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
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
}
