package py.com.kalpa.springsecurityldap.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(name = "cargo_nombre_uk", columnNames = "nombre"))
public class Cargo implements Serializable {

	private static final long serialVersionUID = -5754828217971263261L;

	@Id
	@GeneratedValue
	private Long id;

	private String nombre;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_modificacion")
	@LastModifiedDate
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_creacion" , nullable = false, updatable = false)
    @CreatedBy
    private String usuarioCreacion;
 
    @Column(name = "usuario_modificacion")
    @LastModifiedBy
    private String usuarioModificacion;
     

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
