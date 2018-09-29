package py.com.kalpa.springsecurityldap.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import py.com.kalpa.springsecurityldap.config.seguridad.SesionInfo;
import py.com.kalpa.springsecurityldap.handler.LoginSuccessHandler;

@RestController
@RequestScope
public class LoginController {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private SesionController sesionController;

	@PostMapping("login")
	public ResponseEntity<SesionInfo> login(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam String username, @RequestParam String password) throws ServletException {

		try {
			request.login(username, password);
			loginSuccessHandler.onAuthenticationSuccess(request, response,
					SecurityContextHolder.getContext().getAuthentication());

			return sesionController.info();
		} catch (ServletException servletExc) {
			logger.log(Level.SEVERE, "Error en login-> usuario: " + username, servletExc);
			HttpHeaders headers = new HttpHeaders();
			headers.add("error", servletExc.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).body(new SesionInfo());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error en login-> usuario: " + username, ex);
			HttpHeaders headers = new HttpHeaders();
			headers.add("error", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(new SesionInfo());
		}
	}

	@PostMapping("cerrar-sesion")
	public ResponseEntity<SesionInfo> logout(ModelMap model, HttpServletRequest request) {

		logger.info("Cerrando sesión");
		SecurityContextHolder.clearContext();
		request.getSession().invalidate();
		return ResponseEntity.ok(new SesionInfo());
	}

}
