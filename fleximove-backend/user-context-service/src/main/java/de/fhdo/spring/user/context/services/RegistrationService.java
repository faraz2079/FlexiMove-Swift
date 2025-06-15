package de.fhdo.spring.user.context.services;

import de.fhdo.spring.user.context.clients.BookingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.Password;
import de.fhdo.spring.user.context.domain.User;
import de.fhdo.spring.user.context.repository.UserRepository;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	public void registerUser(User user) {
        // Prüfen, ob der Benutzer bereits existiert
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("Benutzer existiert bereits!");
        }

        // Benutzer als registriert markieren
        user.setRegisterd(true);

        // Benutzer speichern
        userRepository.save(user);
    }
}