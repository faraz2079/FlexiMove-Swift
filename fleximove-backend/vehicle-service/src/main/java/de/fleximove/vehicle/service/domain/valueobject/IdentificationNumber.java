package de.fleximove.vehicle.service.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class IdentificationNumber {
    @Column(name = "identification_number", unique = true, nullable = false)
    private String identNumber;

    public IdentificationNumber(String value) {
        //TODO: Maybe, compare with some Regex
        this.identNumber = value;
    }
}

