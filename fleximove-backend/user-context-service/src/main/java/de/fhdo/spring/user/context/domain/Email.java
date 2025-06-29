package de.fhdo.spring.user.context.domain;

import java.util.Objects;
import java.util.regex.Pattern;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Email {

	 private static final Pattern EMAIL_PATTERN = Pattern.compile(
		        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
		        Pattern.CASE_INSENSITIVE
		    );

	 private String value;

	 public Email(String value) {
		 if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
			 throw new IllegalArgumentException("Invalid email address: " + value);
		 }
		 this.value = value.toLowerCase();
	 }

	 @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
		 if (!(o instanceof Email)) return false;
		 Email email = (Email) o;
		 return value.equals(email.value);
	 }

	@Override
	public String toString() {
		return value;
	}
	 @Override
	 public int hashCode() {
		        return Objects.hash(value);
		    }
		
}
