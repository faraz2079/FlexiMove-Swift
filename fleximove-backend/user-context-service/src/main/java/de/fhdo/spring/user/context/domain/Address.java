package de.fhdo.spring.user.context.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
public class Address {

	private String street;
	private String country;
	private String city;
	private String streetNumber;
	private String postcode;

	public Address(String street, String country, String city, String streetNumber, String postcode) {
		this.street= Objects.requireNonNull(street, "street must not be null");;
		this.country=Objects.requireNonNull(country, "country must not be null");;
		this.city=Objects.requireNonNull(city, "city must not be null");;
		this.streetNumber=Objects.requireNonNull(streetNumber, "streetNumber must not be null");;
		this.postcode=Objects.requireNonNull(postcode, "postcode must not be null");;
	}
}
