package de.fhdo.spring.user.context.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Adress {
public String street;
public String country;
public String city;
public String number;
public String postcode;

protected Adress() {
}
public Adress(String street, String country,String city,String number,String postcode) {
	
	if (street == null || street.isEmpty()) {
        throw new IllegalArgumentException("Stree can not be empty");
    }
	if (country == null || country.isEmpty()) {
        throw new IllegalArgumentException("Stree can not be empty");
    }
	if (city == null || city.isEmpty()) {
        throw new IllegalArgumentException("Stree can not be empty");
    }
	if (number == null || number.isEmpty()) {
        throw new IllegalArgumentException("Stree can not be empty");
    }
	if (postcode == null || postcode.isEmpty()) {
        throw new IllegalArgumentException("Stree can not be empty");
    }
	this.street=street;
	this.country=country;
	this.city=city;
	this.number=number;
	this.postcode=postcode;
}


public String getStreet() {
	return street;
}


public String getCountry() {
	return country;
}


public String getCity() {
	return city;
}


public String getNumber() {
	return number;
}


public String getPostcode() {
	return postcode;
}




}
