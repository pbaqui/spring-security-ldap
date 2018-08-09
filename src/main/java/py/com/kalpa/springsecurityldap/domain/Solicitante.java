package py.com.kalpa.springsecurityldap.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "solicitante_cedula_uk", columnNames = "cedula"))
public class Solicitante implements Serializable {

	private static final long serialVersionUID = 8849003562216023017L;

	@Id
	@GeneratedValue
	private Long id;

	@Size(max = 18, message = "{solicitante.cedula.size}")
	private String cedula;

	@Size(max = 100, message = "{solicitante.nombre.size}")
	private String nombre;

	@Size(max = 100, message = "{solicitante.apellido.size}")
	private String apellido;

	@Size(max = 100, message = "{solicitante.direccion.size}")
	private String direccion;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "solicitante_cargo_fk"))
	@NotNull(message = "{solicitante.cargo.notNull}")
	private Cargo cargo;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "solicitante_departamento_fk"))
	@NotNull(message = "{solicitante.departamento.notNull}")
	private Departamento departamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	@Override
	public String toString() {
		return "Solicitante [cedula=" + cedula + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}

}
