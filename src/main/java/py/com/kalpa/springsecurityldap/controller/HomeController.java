package py.com.kalpa.springsecurityldap.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestScope
@RequestMapping("/")
public class HomeController {

    private static Logger log = LoggerFactory.getLogger(HomeController.class);

	@GetMapping
	public String index() {
		return "<h1> Bienvenido " + new Date() + "! </h1>";
		
		
	}
}
