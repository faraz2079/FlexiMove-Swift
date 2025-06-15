package de.fhdo.spring.user.context.controllers;

import java.util.List;

import de.fhdo.spring.user.context.domain.*;
import de.fhdo.spring.user.context.dto.PasswordChangeRequest;
import de.fhdo.spring.user.context.dto.PersonalInfoUpdateRequest;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import de.fhdo.spring.user.context.clients.BookingClient;
import de.fhdo.spring.user.context.clients.VehicleClient;

import de.fhdo.spring.user.context.dto.LoginRequest;
import de.fhdo.spring.user.context.services.LoginService;
import de.fhdo.spring.user.context.services.RegistrationService;
import de.fhdo.spring.user.context.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
    private final LoginService loginService;
    private final RegistrationService registrationService;
    private final BookingClient bookingClient;
    private final VehicleClient vehicleClient;

    @Autowired
    public UserController(UserService userService, LoginService loginService, RegistrationService registrationService, BookingClient bookingClient,VehicleClient vehicleClient) {
        this.userService = userService;
        this.loginService = loginService;
        this.registrationService = registrationService;
        this.bookingClient = bookingClient;
        this.vehicleClient=vehicleClient;
    }


	// Alle User abrufen
	@GetMapping
	public List<User> getAlleUser() {
		return userService.getAlleUser();
	}

	// User nach ID abrufen
	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
	
	@DeleteMapping("/deleteAccount/{userId}")
	public ResponseEntity<Void> deleteUserWithDependencies(@PathVariable Long userId) {
	    try {
	        User user = userService.getUserById(userId);
	        if (user == null) {
	            return ResponseEntity.notFound().build();
	        }
	        if (user instanceof Customer) {
	            bookingClient.deleteUserBookings(userId);
	        } else if (user instanceof Provider) {
	            vehicleClient.deleteVehicle(userId);
	        } else {
	            return ResponseEntity.badRequest().build();
	        }
	        userService.deleteUserById(userId);
	        return ResponseEntity.noContent().build();

	    } catch (Exception e) {
	        System.err.println("Error during deletion of account: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
	    }
	}

	// Change Password
	@PatchMapping("/{id}/password")
	public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		if (!userService.isPasswordCorrect(user, request.oldPassword)) {
			return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body("Old password is incorrect");
		}

		userService.updatepw(user, request.newPassword);
		return ResponseEntity.ok().build();
	}

	//Change Email
	@PatchMapping("/{id}/email")
	public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody String newEmail) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		if (userService.emailExists(newEmail)) {
			return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("Email is already in use");
		}

		userService.updateEmail(user, newEmail);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{id}/personal-info")
	public ResponseEntity<?> updatePersonalInfo(@PathVariable Long id, @RequestBody PersonalInfoUpdateRequest request) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		userService.updatePersonalInfo(user, request);
		return ResponseEntity.ok().build();
	}


	//Change Adress
	@PatchMapping("/{id}/address")
	public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Address newAddress) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		userService.updateAddress(user, newAddress);
		return ResponseEntity.ok().build();
	}

	//Change payment information
	@PutMapping("/{id}/payment-info")
	public ResponseEntity<?> updatePaymentInfo(@PathVariable Long id, @RequestBody PaymentInfo newPaymentInfo) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		userService.updatePaymentInfo(user, newPaymentInfo);
		return ResponseEntity.ok().build();
	}


	// Company Name ändern (nur für Provider)
	@PatchMapping("/provider/{id}/companyname")
	public void updateCompanyName(@PathVariable Long id, @RequestBody String newCompanyName) {
		User user = userService.getUserById(id);
		if (user instanceof Provider) {
			Provider provider = (Provider) user;
			userService.updtaecompanyname(provider, newCompanyName);
		}
	}
	
	@PostMapping("/registercustomer")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        try {
            registrationService.registerUser(customer);
            return ResponseEntity.ok("Registration successful!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
	
	@PostMapping("/registerprovider")
    public ResponseEntity<String> registerProvider(@RequestBody Provider provider) {
        try {
            registrationService.registerUser(provider);
            return ResponseEntity.ok("Registration successful!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
	    try {
	        User loggedUser = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
			//TODO: nicht kompletten User mit Password zurueckgeben, sondern LoginResponse
	        return ResponseEntity.ok(loggedUser);
	    } catch (IllegalStateException e) {
	        return ResponseEntity.status(401).body("Invalid credentials");
	    }
	}
	
	@GetMapping("/provider/{id}/companyName")
    public ResponseEntity<String> getProviderCompanyName(@PathVariable Long id) {
        String companyName = userService.getCompanyNameById(id);

        if (companyName != null) {
            return ResponseEntity.ok(companyName);
        } else {
            return ResponseEntity.status(404).body("Kein Provider mit dieser ID gefunden.");
        }
    }
	

}