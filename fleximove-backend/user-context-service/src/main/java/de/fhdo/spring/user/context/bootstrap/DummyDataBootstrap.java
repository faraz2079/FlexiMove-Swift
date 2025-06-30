package de.fhdo.spring.user.context.bootstrap;

import java.time.LocalDate;
import de.fhdo.spring.user.context.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.fhdo.spring.user.context.repository.UserRepository;

@Component
public class DummyDataBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DummyDataBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        Address address1 = new Address("Musterstraße", "Deutschland", "Berlin", "15", "10115");
        Password password1 = new Password(passwordEncoder.encode("sicherespasswort123"));
        Email email1 = new Email("john.wick@continental.com");
        PaymentInfo paymentInfo1 = new PaymentInfo("1234567890123456", "John Wick", "12/27", "123");
        User customer1 = new Customer(
                "John",
                "Wick",
                password1,
                email1,
                LocalDate.of(1990, 5, 15),
                address1,
                DriverLicenseType.B,
                "0123456789",
                paymentInfo1
        );

        userRepository.save(customer1);

        Address address2 = new Address("An der Palmweide", "Deutschland", "Dortmund", "15", "10115");
        Password password2 = new Password(passwordEncoder.encode("wirklichsicherespasswort123"));
        Email email2 = new Email("real.britney@google.com");
        PaymentInfo paymentInfo2 = new PaymentInfo("987635612783892", "Britney Spears", "12/27", "123");
        User customer2 = new Customer(
                "Britney",
                "Spears",
                password2,
                email2,
                LocalDate.of(2007, 10, 15),
                address2,
                DriverLicenseType.NONE,
                "0987654321",
                paymentInfo2
        );

        userRepository.save(customer2);

        Address providerAddress1 = new Address("Blumenweg", "Deutschland", "München", "22", "80331");
        Password providerPassword1 = new Password(passwordEncoder.encode("providerpass!2024"));
        Email providerEmail1 = new Email("kontakt@flowerhub.de");
        PaymentInfo providerPayment1 = new PaymentInfo("4111111111111111", "Florian Blume", "11/26", "456");
        User provider1 = new Provider(
                providerPassword1,
                providerEmail1,
                providerAddress1,
                "0891234567",
                "AutoHub GmbH",
                providerPayment1
        );

        userRepository.save(provider1);

        Address providerAddress2 = new Address("Karolinenstrasse", "Deutschland", "Hamburg", "1", "20357");
        Password providerPassword2 = new Password(passwordEncoder.encode("europcar2025"));
        Email providerEmail2 = new Email("business.service@europcar.com");
        PaymentInfo providerPayment2 = new PaymentInfo("4111111111111111", "Florian Blume", "11/26", "456");
        User provider2 = new Provider(
                providerPassword2,
                providerEmail2,
                providerAddress2,
                "035612487665",
                "Europcar",
                providerPayment2
        );

        userRepository.save(provider2);
        
    }
}
