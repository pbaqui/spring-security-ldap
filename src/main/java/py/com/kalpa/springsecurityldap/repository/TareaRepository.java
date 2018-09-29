package py.com.kalpa.springsecurityldap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.kalpa.springsecurityldap.domain.Tarea;
import py.com.kalpa.springsecurityldap.domain.TipoTarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

	List<Tarea> findAllByTipoTareaAndAnhoAndMes(TipoTarea tipoTarea, Integer anho, Integer mes);

	Long countByAnhoAndMes(Integer anho, Integer mes);

	// SELECT * FROM tarea WHERE estado = ?
	List<Tarea> findAllByEstado(String estado);
}
