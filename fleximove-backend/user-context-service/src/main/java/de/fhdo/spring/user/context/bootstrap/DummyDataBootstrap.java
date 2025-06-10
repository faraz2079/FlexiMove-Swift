package de.fhdo.spring.user.context.bootstrap;

import java.time.LocalDate;
import de.fhdo.spring.user.context.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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
    	long count = userRepository.count();
    	System.out.println("initData called — users in DB: " + count);
    	if (count > 0) return;
        userRepository.deleteAll();
    	 // Adresse erstellen
        Address address = new Address("Musterstraße", "Deutschland", "Berlin", "15", "10115");

        // Passwort erstellen
        Password password = new Password("sicheresPasswort123");

        // Email erstellen
        Email email = new Email("kunde1@example.com");

        // Zahlungsinformationen erstellen
        PaymentInfo paymentInfo = new PaymentInfo("1234567890123456", "Max Mustermann", "12/27", "123");

        // Customer erstellen
        User customer1 = new Customer(
                "John",
                "Wick",
                password,
                email,
                LocalDate.of(1990, 5, 15),
                address,
                DriverLicenseType.NONE,
                "0123456789",
                paymentInfo
        );

        userRepository.save(customer1);

        // Adresse für Provider
        Address providerAddress = new Address("Blumenweg", "Deutschland", "München", "22", "80331");

        // Passwort & Email für Provider
        Password providerPassword = new Password("providerPass!2024");
        Email providerEmail = new Email("kontakt@flowerhub.de");

        // Zahlungsinformationen für Provider
        PaymentInfo providerPayment = new PaymentInfo("4111111111111111", "Florian Blume", "11/26", "456");

        // Provider erstellen
        User provider = new Provider(
                providerPassword,
                providerEmail,
                providerAddress,
                "0891234567",
                "AutoHub GmbH",
                providerPayment
        );

        userRepository.save(provider);
        
    }
}
