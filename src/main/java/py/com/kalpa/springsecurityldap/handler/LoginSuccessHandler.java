package py.com.kalpa.springsecurityldap.handler;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import py.com.kalpa.springsecurityldap.config.seguridad.SesionUsuario;
import py.com.kalpa.springsecurityldap.domain.LoginInfo;
import py.com.kalpa.springsecurityldap.domain.Usuario;
import py.com.kalpa.springsecurityldap.repository.LoginInfoRepository;
import py.com.kalpa.springsecurityldap.repository.UsuarioRepository;

/**
 * Se ejecuta cuando usuario inicia sesión correctamente
 */
@Service
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private LoginInfoRepository loginRepo;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		SesionUsuario sesionUsuario = appContext.getBean(SesionUsuario.class);
		sesionUsuario.confirmar();
		LoginInfo loginInfo = new LoginInfo();
		// SesionInfo sesionInfo = sesionUsuario.getSesionInfo();
		// loginInfo.setUsuario(sesionInfo.getUsuario().getCodigo());
		loginInfo.setUsuario(request.getUserPrincipal().getName());
		loginInfo.setFechaHora(new Date());
		String userAgent = request.getHeader("User-Agent");
		int max = userAgent.length();
		if (max > 250) {
			max = 250;
		}
		loginInfo.setInfo(userAgent.substring(0, max));
		loginRepo.save(loginInfo);

	}

}
