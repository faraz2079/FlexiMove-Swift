package de.fhdo.spring.user.context.services;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fhdo.spring.user.context.domain.Adress;
import de.fhdo.spring.user.context.domain.Email;
import de.fhdo.spring.user.context.domain.Password;
import de.fhdo.spring.user.context.domain.Provider;
import de.fhdo.spring.user.context.domain.User;
import de.fhdo.spring.user.context.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;

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
	
	//User löschen
	public void deleteUser(User user){
		userRepository.delete(user);
		
	}
	
	//User Passwort ändern
	public void updatepw(User user, Password pw) {
		user.setPassword(pw);
		userRepository.save(user);
	}
	
	//Adresse ändern
	public void updateadress(User user, Adress adress) {
		user.setAddress(adress);
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
