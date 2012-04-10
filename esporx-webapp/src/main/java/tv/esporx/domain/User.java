package tv.esporx.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.GenerationType.IDENTITY;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class User {
	@NotBlank
	@Size(max = 255)
	private String email = "";
	@NotBlank
	@Size(max = 100)
	private String firstName = "";
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	@NotBlank
	@Size(max = 100)
	private String lastName = "";
	@NotBlank
	@Size(max = 100)
	private String nickname = "";
	@Size(min = 6, max = 30)
	private String password = "";

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(final String email) {
		checkArgument(email != null);
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		}
		catch (AddressException e) {
			throw new IllegalArgumentException(e);
		}
		this.email = email;
	}

	public void setFirstName(final String firstName) {
		checkArgument(firstName != null);
		this.firstName = firstName;
	}

	public void setLastName(final String lastName) {
		checkArgument(lastName != null);
		this.lastName = lastName;
	}

	public void setNickname(final String nickname) {
		checkArgument(nickname != null);
		this.nickname = nickname;
	}

	public void setPassword(final String password) {
		checkArgument(password != null);
		this.password = password;
	}

}
