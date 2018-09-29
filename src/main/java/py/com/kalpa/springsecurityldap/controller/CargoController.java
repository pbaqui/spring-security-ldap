package py.com.kalpa.springsecurityldap.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.com.kalpa.springsecurityldap.domain.Cargo;
import py.com.kalpa.springsecurityldap.repository.CargoRepository;

@RestController
@RequestMapping("cargo")
public class CargoController {

	@Autowired
	private CargoRepository repository;

	protected Logger logger = Logger.getLogger(getClass().getName());

//	@Secured("ROLE_ADMIN")
	@PostMapping
	public ResponseEntity<Cargo> save(@RequestBody Cargo entity) {
		logger.info("Guardando registro: " + entity);
		repository.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entity);
	}

//	@PreAuthorize("hasAnyRole('ADMIN','DESARROLLADOR')")
	@GetMapping
	public ResponseEntity<List<Cargo>> getList() {
		return ResponseEntity.ok(repository.findAll());
	}

//	@PreAuthorize("hasAuthority('LISTAR_CARGO')") 
	@GetMapping("{id}")
	public ResponseEntity<Cargo> find(@PathVariable Long id) {
		Cargo entity = repository.findOne(id);
		if (entity == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(entity);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Cargo> delete(@PathVariable Long id) {
		logger.info("Buscando entidad con id: " + id);
		Cargo entity = repository.findOne(id);
		if (entity == null) {
			logger.info("No se encontró entidad con id: " + id);
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
		logger.info("Borrando entidad: " + entity);
		repository.delete(entity);
		return ResponseEntity.ok(entity);
	}

}
