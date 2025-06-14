package de.fhdo.spring.user.context.dto;

import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.Password;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
	private Email email;
    private Password password;
    
    
    
	public Email getEmail() {
		return email;
	}
	public Password getPassword() {
		return password;
	}
}

