package py.com.kalpa.springsecurityldap.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Tarea {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@NotNull(message = "Debe ingresar solicitante")
	@JoinColumn(foreignKey = @ForeignKey(name = "tarea_solicitante_fk"))
	private Solicitante solicitante;

	// @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date fechaSolicitud;

	@ManyToOne
	@NotNull(message = "Seleccione tipo tarea")
	@JoinColumn(foreignKey = @ForeignKey(name = "tarea_tipotarea_fk"))
	private TipoTarea tipoTarea;

	@NotNull
	@Min(value = 2000)
	private Integer anho;

	@NotNull
	@Min(value = 1)
	@Max(value = 12)
	private Integer mes;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "tarea_personal_fk"))
	private Personal personalAsignado;

	// P: Pendiente, F: Finalizada, R: Rechazada
	@NotNull
	@Size(max = 1)
	private String estado = "P";
	@Size(max = 100)
	private String observacion;

	public Tarea() {
		Calendar cal = Calendar.getInstance();
		fechaSolicitud = cal.getTime();
		anho = cal.get(Calendar.YEAR);
		// 0 - 11 -> Enero= 0, Diciembre=11
		mes = cal.get(Calendar.MONTH) + 1;
		estado = "P";// ya se inicializó
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Solicitante getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Solicitante solicitante) {
		this.solicitante = solicitante;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public TipoTarea getTipoTarea() {
		return tipoTarea;
	}

	public void setTipoTarea(TipoTarea tipoTarea) {
		this.tipoTarea = tipoTarea;
	}

	public Integer getAnho() {
		return anho;
	}

	public void setAnho(Integer anho) {
		this.anho = anho;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Personal getPersonalAsignado() {
		return personalAsignado;
	}

	public void setPersonalAsignado(Personal personalAsignado) {
		this.personalAsignado = personalAsignado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getFechaSolicitudStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		if (fechaSolicitud != null) {
			return sdf.format(fechaSolicitud);
		}
		return null;
	}
	// partials/tarea/tareas_pendientes.html
	// app.js , tarea.controller.js -> incluir en index.html
	// tareas-pendientes
}
