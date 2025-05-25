package de.fhdo.spring.user.context.dto;

import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.Password;

public class LoginRequest {
	private Email email;
    private Password password;

    // Standard-Konstruktor (wichtig für Spring)
    public LoginRequest() {}

    // Getter und Setter
    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }
    public void setPassword(Password password) {
        this.password = password;
    }
}

