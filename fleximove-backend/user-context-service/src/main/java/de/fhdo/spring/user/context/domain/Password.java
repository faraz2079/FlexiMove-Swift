package de.fhdo.spring.user.context.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Password {
    private  String value;

    public Password(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Password can't be empty");
        }
        if (value.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password = (Password) o;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "****";
    }
}