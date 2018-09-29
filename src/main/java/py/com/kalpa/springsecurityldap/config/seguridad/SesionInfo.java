package py.com.kalpa.springsecurityldap.config.seguridad;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import py.com.kalpa.springsecurityldap.domain.Permiso;
import py.com.kalpa.springsecurityldap.domain.Usuario;


public class SesionInfo {
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date fechaHoraInicio;
	private String usuario;
	private List<Permiso> permisos;

	public Date getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(Date fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

}
