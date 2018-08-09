package py.com.kalpa.springsecurityldap.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.com.kalpa.springsecurityldap.domain.Departamento;
import py.com.kalpa.springsecurityldap.repository.DepartamentoRepository;

@RestController
@RequestMapping("departamento")
public class DepartamentoController {

	@Autowired
	private DepartamentoRepository repository;

	protected Logger logger = Logger.getLogger(getClass().getName());

	@Secured({"ROLE_ADMIN","ROLE_DESARROLLADOR"})
	@PostMapping
	public ResponseEntity<Departamento> save(@RequestBody Departamento entity) {
		logger.info("Guardando registro: " + entity);
		repository.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entity);
	}

	@Secured({"ROLE_ADMIN","ROLE_DESARROLLADOR"})
	@GetMapping
	public ResponseEntity<List<Departamento>> getList() {
		return ResponseEntity.ok(repository.findAll());
	}

	
	@GetMapping("{id}")
	public ResponseEntity<Departamento> find(@PathVariable Long id) {
		Departamento entity = repository.findOne(id);
		if (entity == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(entity);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Departamento> delete(@PathVariable Long id) {
		logger.info("Buscando entidad con id: " + id);
		Departamento entity = repository.findOne(id);
		if (entity == null) {
			logger.info("No se encontró entidad con id: " + id);
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
		logger.info("Borrando entidad: " + entity);
		repository.delete(entity);
		return ResponseEntity.ok(entity);
	}


}
