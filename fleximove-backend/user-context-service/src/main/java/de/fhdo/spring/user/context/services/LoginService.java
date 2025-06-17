package de.fhdo.spring.user.context.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.User;
import de.fhdo.spring.user.context.repository.UserRepository;
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(new Email(email));
        if (user == null) {
            throw new IllegalStateException("User doesn't exist");
        }
        
        if (!user.isRegistered()) {
            throw new IllegalStateException("User is not registered");
        }

        if (!passwordEncoder.matches(password, user.getPassword().getValue())) {
            throw new IllegalStateException("Invalid password");
        }
        return user;
    }
}
