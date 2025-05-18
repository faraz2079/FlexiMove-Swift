package de.fleximove.vehicle.service.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class IdentificationNumber {
    @Column(name = "identification_number", unique = true, nullable = false)
    private String identNumber;

    protected IdentificationNumber() {}

    public IdentificationNumber(String value) {
        //TODO: Maybe, compare with some Regex
        this.identNumber = value;
    }

    public String getIdentNumber() {
        return identNumber;
    }
}

