package py.com.kalpa.springsecurityldap.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.com.kalpa.springsecurityldap.domain.Solicitante;
import py.com.kalpa.springsecurityldap.repository.SolicitanteRepository;

@RestController
@RequestMapping("solicitante")
public class SolicitanteController {
	@Autowired
	private SolicitanteRepository solicitanteRepo;

	protected Logger logger = Logger.getLogger(getClass().getName());

	@PostMapping
	public ResponseEntity<Solicitante> save(@RequestBody Solicitante entity) {
		logger.info("Guardando registro: " + entity);
		solicitanteRepo.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entity);
	}

	@PreAuthorize("hasAuthority('LISTAR_SOLICITANTE')")
	@GetMapping
	public ResponseEntity<List<Solicitante>> getList() {
		return ResponseEntity.ok(solicitanteRepo.findAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<Solicitante> find(@PathVariable Long id) {
		Solicitante entity = solicitanteRepo.findById(id).orElse(null);
		if (entity == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(entity);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Solicitante> delete(@PathVariable Long id) {
		logger.info("Buscando entidad con id: " + id);
		Solicitante entity = solicitanteRepo.findById(id).orElse(null);
		if (entity == null) {
			logger.info("No se encontró entidad con id: " + id);
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
		logger.info("Borrando entidad: " + entity);
		solicitanteRepo.delete(entity);
		return ResponseEntity.ok(entity);
	}

}
