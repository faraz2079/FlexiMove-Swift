package de.fhdo.spring.user.context.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.fhdo.spring.user.context.domain.Password;
import de.fhdo.spring.user.context.domain.User;
import de.fhdo.spring.user.context.repository.UserRepository;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

	public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("User already exists!");
        }

        String hashed = passwordEncoder.encode(user.getPassword().getValue());
        user.setPassword(new Password(hashed));

        user.setRegistered(true);

        userRepository.save(user);
    }
}