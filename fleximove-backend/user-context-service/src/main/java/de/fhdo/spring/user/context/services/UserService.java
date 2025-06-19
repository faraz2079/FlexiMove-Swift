package de.fhdo.spring.user.context.services;

import java.util.ArrayList;


import java.util.List;
import java.util.Optional;

import de.fhdo.spring.user.context.clients.RatingClient;
import de.fhdo.spring.user.context.clients.VehicleClient;
import de.fhdo.spring.user.context.domain.*;
import de.fhdo.spring.user.context.dto.PersonalInfoUpdateRequest;
import de.fhdo.spring.user.context.dto.ProviderPersonalInfoUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import de.fhdo.spring.user.context.domain.Address;

import de.fhdo.spring.user.context.clients.BookingClient;
import de.fhdo.spring.user.context.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final BookingClient bookingClient;
	private final PasswordEncoder passwordEncoder;
	private final VehicleClient vehicleClient;
	private final RatingClient ratingClient;

	@Autowired
	public UserService(UserRepository userRepository, BookingClient bookingClient, PasswordEncoder passwordEncoder, VehicleClient vehicleClient, RatingClient ratingClient) {
	    this.userRepository = userRepository;
	    this.bookingClient = bookingClient;
		this.passwordEncoder = passwordEncoder;
		this.vehicleClient = vehicleClient;
		this.ratingClient = ratingClient;
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
	
	public User getUserByEmail(String email) {
	    User user = this.userRepository.findByEmail(new Email(email));
	    return user != null ? user : null;
	}

	
   //User speichern
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	
	
	public void deleteUserById(Long userId) {
	    Optional<User> optionalUser = userRepository.findById(userId);
	    if (optionalUser.isPresent()) {
	        userRepository.deleteById(userId);
	    } else {
	        throw new EntityNotFoundException("User mit ID " + userId + " nicht gefunden.");
	    }
	}

	public void updatepw(User user, String newPasswordPlaintext) {
		String hashed = passwordEncoder.encode(newPasswordPlaintext);
		Password pw = new Password(hashed);
		user.setPassword(pw);
		userRepository.save(user);
	}

	public void updateEmail(User user, String newEmail) {
		Email email = new Email(newEmail);
		user.setEmail(email);
		userRepository.save(user);
	}

	public boolean emailExists(String newEmail) {
		Email newUserEmail = new Email(newEmail);
		return userRepository.findByEmail(newUserEmail) != null;
	}

	public void updatePersonalInfo(User user, PersonalInfoUpdateRequest request) {
		user.setPhoneNumber(request.getPhoneNumber());

		if (user instanceof Customer) {
			((Customer) user).setDriverLicenseType(request.getDriverLicenseType());
			((Customer) user).setFirstName(request.getFirstName());
			((Customer) user).setLastName(request.getLastName());
			((Customer) user).setDateOfBirth(request.getDateOfBirth());
		}

		userRepository.save(user);
	}

	public void updateProviderPersonalInfo(User user, ProviderPersonalInfoUpdateRequest request) {
		user.setPhoneNumber(request.getPhoneNumber());

		if (user instanceof Provider) {
			((Provider) user).setCompanyName(request.getCompanyName());
		}

		userRepository.save(user);
	}

	public void updatePaymentInfo(User user, PaymentInfo newInfo) {
		user.setPaymentinfo(newInfo);
		userRepository.save(user);
	}

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

	public boolean isPasswordCorrect(User user, String inputPassword) {
		return passwordEncoder.matches(inputPassword, user.getPassword().getValue());
	}

	@Transactional
	public void deleteUserAndDependencies(Long userId) {
		User user = getUserById(userId);
		if (user == null) {
			throw new EntityNotFoundException("User not found with ID: " + userId);
		}

		if (user instanceof Customer) {
			ratingClient.deleteCustomerRatingsByUserId(userId);
			bookingClient.deleteUserBookings(userId);
		} else if (user instanceof Provider) {
			ratingClient.deleteAllProviderRatings(userId);
			vehicleClient.deleteVehicle(userId);
		} else {
			throw new IllegalArgumentException("Unsupported user type.");
		}

		deleteUserById(userId);
	}

}
