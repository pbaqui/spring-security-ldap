package py.com.kalpa.springsecurityldap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.kalpa.springsecurityldap.domain.TipoTarea;
import py.com.kalpa.springsecurityldap.repository.TipoTareaRepository;


@RestController
@RequestMapping("tipos-tareas")
public class TipoTareaController extends GenericController<TipoTarea> {
	@Autowired
	private TipoTareaRepository repo;

	@Override
	public JpaRepository<TipoTarea, Long> getRepository() {
		return repo;
	}

}
