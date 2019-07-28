package py.com.kalpa.springsecurityldap.controller;

import java.security.Principal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestScope
@RequestMapping("/")
public class HomeController {

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

	@GetMapping
	public String index(Authentication auth, Principal principal) {
		
		//Authentication auth = SecurityContextHolde.getContext().getAuthentication();
		
		return "<h1> Bienvenido " + auth.getName()  + " la fecha es: " +new Date() + "! </h1>" +
		"<h1> Sus Roles:  " + auth.getAuthorities()  +"</h1>"+
				"<h1> Principal:  " + principal.getName() +"</h1>";
		
		
	}
}
