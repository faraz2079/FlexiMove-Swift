package de.fhdo.spring.user.context.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.Provider;
import de.fhdo.spring.user.context.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(Email email);
    Optional<Provider> findBycompanyName(String companyName);
}
