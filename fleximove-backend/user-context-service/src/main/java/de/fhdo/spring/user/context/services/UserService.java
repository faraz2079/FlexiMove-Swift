package de.fhdo.spring.user.context.services;

import java.util.ArrayList;


import java.util.List;
import java.util.Optional;

import de.fhdo.spring.user.context.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.fhdo.spring.user.context.domain.Address;

import de.fhdo.spring.user.context.clients.BookingClient;
import de.fhdo.spring.user.context.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final BookingClient bookingClient;



	@Autowired
	public UserService(UserRepository userRepository, BookingClient bookingClient) {
	    this.userRepository = userRepository;
	    this.bookingClient = bookingClient;
	}

	//Alle User
	public List<User> getAlleUser() {
		List<User> user = new ArrayList<User>();
		this.userRepository.findAll().forEach(user::add);
		return user;
	}

	//User mit ID rausholen
	public User getUserById(Long id) {
		 Optional<User> user = this.userRepository.findById(id);
		 return user.isPresent() ? user.get() : null; 
	}
	
	public User getUserByEmail(Email email) {
	    User user = this.userRepository.findByEmail(email);
	    return user != null ? user : null;
	}

	
   //User speichern
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	
	
	public void deleteUserById(Long userId) {
	    Optional<User> optionalUser = userRepository.findById(userId);
	    if (optionalUser.isPresent()) {
	        // 1. Buchungen löschen
	        bookingClient.deleteUserBookings(userId);

	        // 2. User löschen
	        userRepository.deleteById(userId);
	    } else {
	        throw new EntityNotFoundException("User mit ID " + userId + " nicht gefunden.");
	    }
	}

	
	
	
	//User Passwort ändern
	public void updatepw(User user, Password pw) {
		user.setPassword(pw);
		userRepository.save(user);
	}
	
	//Adresse ändern
	public void updateAddress(User user, Address address) {
		user.setAddress(address);
		userRepository.save(user);
	}
	
	//Company-Name ändern
	public void updtaecompanyname(Provider provider, String companyName) {
		provider.setCompanyName(companyName);
		userRepository.save(provider);
	}
	
	public String getCompanyNameById(Long id) {
	    Optional<User> optionalUser = userRepository.findById(id);
	    
	    if (optionalUser.isPresent() && optionalUser.get() instanceof Provider) {
	        Provider provider = (Provider) optionalUser.get();
	        return provider.getCompanyName();
	    }

	    // Optional: Fehlerbehandlung oder null zurückgeben
	    return null;
	}


}
