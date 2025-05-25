package de.fhdo.spring.user.context.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Password {
    private  String value;

    
    protected Password() {
    	
    }

    public Password(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Passwort darf nicht leer sein");
        }
        if (value.length() < 8) {
            throw new IllegalArgumentException("Passwort muss mindestens 8 Zeichen lang sein");
        }
        // Hier kannst du noch weitere Checks einbauen (Großbuchstaben, Zahlen, Sonderzeichen, etc.)
        this.value = value;
    }

    public String getValue() {
        return value;
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
        return "****";  // Passwort nicht im Klartext ausgeben!
    }
}
