package de.fhdo.spring.user.context.controllers;

import java.util.List;

import de.fhdo.spring.user.context.domain.*;
import de.fhdo.spring.user.context.dto.PasswordChangeRequest;
import de.fhdo.spring.user.context.dto.PersonalInfoUpdateRequest;
import de.fhdo.spring.user.context.dto.ProviderPersonalInfoUpdateRequest;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public UserController(UserService userService, LoginService loginService, RegistrationService registrationService) {
        this.userService = userService;
        this.loginService = loginService;
        this.registrationService = registrationService;
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
	public ResponseEntity<?> deleteUserWithDependencies(@PathVariable Long userId) {
	    try {
			userService.deleteUserAndDependencies(userId);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("User not found.");
		} catch (FeignException | IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			System.err.println("Error during deletion of account: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.body("Unexpected error occurred while deleting user.");
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

	@PatchMapping("/{id}/provider-personal-info")
	public ResponseEntity<?> updateProviderPersonalInfo(@PathVariable Long id, @RequestBody ProviderPersonalInfoUpdateRequest request) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		userService.updateProviderPersonalInfo(user, request);
		return ResponseEntity.ok().build();
	}

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