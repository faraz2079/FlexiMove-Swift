package de.fhdo.spring.user.context.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.Password;
import de.fhdo.spring.user.context.domain.User;
import de.fhdo.spring.user.context.repository.UserRepository;
@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public boolean login(Email email, Password password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalStateException("Benutzer existiert nicht");
        }
        
        if (!user.isRegistered()) {
            throw new IllegalStateException("Benutzer ist nicht registriert");
        }

        if (!user.getPassword().getValue().equals(password)) {
            throw new IllegalStateException("Passwort ist falsch");
        }

        return true; // Login erfolgreich
    }
}
