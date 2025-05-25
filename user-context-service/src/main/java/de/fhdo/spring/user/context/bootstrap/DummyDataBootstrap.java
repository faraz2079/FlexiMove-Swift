package de.fhdo.spring.user.context.bootstrap;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import de.fhdo.spring.user.context.domain.Adress;
import de.fhdo.spring.user.context.domain.Customer;
import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.Password;
import de.fhdo.spring.user.context.domain.PaymentInfo;
import de.fhdo.spring.user.context.domain.Provider;
import de.fhdo.spring.user.context.repository.UserRepository;

@Component
public class DummyDataBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;

    @Autowired
    public DummyDataBootstrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
    	 // Adresse erstellen
        Adress address = new Adress("Musterstraße", "Deutschland", "Berlin", "15", "10115");

        // Passwort erstellen
        Password password = new Password("sicheresPasswort123");

        // Email erstellen
        Email email = new Email("kunde1@example.com");

        // Zahlungsinformationen erstellen
        PaymentInfo paymentInfo = new PaymentInfo("1234567890123456", "Max Mustermann", "12/27", "123");

        // Customer erstellen
        Customer customer1 = new Customer(
                password,
                email,
                LocalDate.of(1990, 5, 15),
                address,
                true,
                "0123456789",
                paymentInfo
        );
        
        userRepository.save(customer1);
        
    }
}
