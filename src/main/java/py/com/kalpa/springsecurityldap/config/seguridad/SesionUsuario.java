package py.com.kalpa.springsecurityldap.config.seguridad;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import py.com.kalpa.springsecurityldap.domain.Permiso;
@Component
@SessionScope
public class SesionUsuario {
	private String usuarioPorConfirmar;
	private List<Permiso> permisosPorConfirmar;
	private SesionInfo sesionInfo;

	public String getUsuarioPorConfirmar() {
		return usuarioPorConfirmar;
	}

	public void setUsuarioPorConfirmar(String usuarioPorConfirmar) {
		this.usuarioPorConfirmar = usuarioPorConfirmar;
	}

	public List<Permiso> getPermisosPorConfirmar() {
		return permisosPorConfirmar;
	}

	public void setPermisosPorConfirmar(List<Permiso> permisosPorConfirmar) {
		this.permisosPorConfirmar = permisosPorConfirmar;
	}

	public SesionInfo getSesionInfo() {
		return sesionInfo;
	}

	/**
	 * invocado desde {@link LoginSuccessHandler}, se asume que credenciales son
	 * correctas
	 */
	public void confirmar() {
		sesionInfo = new SesionInfo();
		sesionInfo.setUsuario(usuarioPorConfirmar);
		sesionInfo.setPermisos(permisosPorConfirmar);
		sesionInfo.setFechaHoraInicio(new Date());
	}

}
