package py.com.kalpa.springsecurityldap.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RoleId implements Serializable {

	private static final long serialVersionUID = 2338598430548079648L;

	@Column(name = "role")
	private String role;

	@Column(name = "username")
	private String username;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}