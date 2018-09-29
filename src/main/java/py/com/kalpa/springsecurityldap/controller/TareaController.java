package py.com.kalpa.springsecurityldap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.com.kalpa.springsecurityldap.domain.Tarea;
import py.com.kalpa.springsecurityldap.domain.TipoTarea;
import py.com.kalpa.springsecurityldap.repository.TareaRepository;
import py.com.kalpa.springsecurityldap.repository.TipoTareaRepository;


@RestController
@RequestMapping("tareas")
public class TareaController extends GenericController<Tarea> {
	@Autowired
	private TareaRepository tareaRepo;
	@Autowired
	private TipoTareaRepository tipoTareaRepo;

	@Override
	public TareaRepository getRepository() {
		return tareaRepo;
	}

	@GetMapping("{anho}/{mes}/{tipoTareaId}")
	public ResponseEntity<List<Tarea>> list(@PathVariable Integer anho, @PathVariable Integer mes,
			@PathVariable Long tipoTareaId) {
		TipoTarea tpo = tipoTareaRepo.findOne(tipoTareaId);

		if (tpo == null) {
			logger.info("No existe tipo Tarea con id: " + tipoTareaId);
			return ResponseEntity.noContent().build();
		}
		List<Tarea> tareas = tareaRepo.findAllByTipoTareaAndAnhoAndMes(tpo, anho, mes);
		return ResponseEntity.ok(tareas);
	}

	@GetMapping("pendientes")
	public ResponseEntity<List<Tarea>> pendientes() {
		List<Tarea> pendienteList = tareaRepo.findAllByEstado("P");
		logger.info("Tareas pendientes: " + pendienteList.size());
		return ResponseEntity.ok(pendienteList);
	}

	@GetMapping("finalizadas")
	public ResponseEntity<List<Tarea>> finalizadas() {
		List<Tarea> pendienteList = tareaRepo.findAllByEstado("F");
		logger.info("Tareas pendientes: " + pendienteList.size());
		return ResponseEntity.ok(pendienteList);
	}

	@GetMapping("rechazadas")
	public ResponseEntity<List<Tarea>> rechazadas() {
		return getListByEstado("R");
	}

	private ResponseEntity<List<Tarea>> getListByEstado(String estado) {
		List<Tarea> list = tareaRepo.findAllByEstado(estado);
		logger.info("Tareas con estado " + estado + ":" + list.size());
		return ResponseEntity.ok(list);
	}

}
