package py.com.kalpa.springsecurityldap.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role implements Serializable {

	private static final long serialVersionUID = 7154369949684441096L;

	@EmbeddedId
	private RoleId id;

	public RoleId getId() {
		return id;
	}

	public void setId(RoleId id) {
		this.id = id;
	}

	@ManyToMany
	@JoinTable(
			name = "roles_permisos", 
			joinColumns = {@JoinColumn(name = "role_role", referencedColumnName = "role"), 
					 @JoinColumn(name = "role_username", referencedColumnName = "username")},
			inverseJoinColumns = @JoinColumn(name = "permiso_id", 
			referencedColumnName = "id"))
	private Collection<Permiso> permisos;

}
