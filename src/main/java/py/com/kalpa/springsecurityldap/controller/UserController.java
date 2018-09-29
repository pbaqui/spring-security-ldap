package py.com.kalpa.springsecurityldap.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.com.kalpa.springsecurityldap.domain.Usuario;
import py.com.kalpa.springsecurityldap.repository.CargoRepository;
import py.com.kalpa.springsecurityldap.repository.UsuarioRepository;

@RestController
@RequestMapping("usuario")
public class UserController {

	@Autowired
	private UsuarioRepository repository;

	protected Logger logger = Logger.getLogger(getClass().getName());

//	@Secured("ROLE_ADMIN")
	@PostMapping
	public ResponseEntity<Usuario> save(@RequestBody Usuario entity) {
		logger.info("Guardando registro: " + entity);
		repository.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entity);
	}

//	@PreAuthorize("hasAnyRole('ADMIN','DESARROLLADOR')")
	@GetMapping
	public ResponseEntity<List<Usuario>> getList() {
		return ResponseEntity.ok(repository.findAll());
	}

//	@PreAuthorize("#username == authentication.name")
	//@PostAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("{username}")
	public ResponseEntity<Usuario> find(@PathVariable String username) {
		Usuario entity = repository.findOne(username);
		if (entity == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(entity);
	}

	@DeleteMapping("{username}")
	public ResponseEntity<Usuario> delete(@PathVariable String username) {
		logger.info("Buscando entidad con id: " + username);
		Usuario entity = repository.findOne(username);
		if (entity == null) {
			logger.info("No se encontró entidad con id: " + username);
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
		logger.info("Borrando entidad: " + entity);
		repository.delete(entity);
		return ResponseEntity.ok(entity);
	}

}
