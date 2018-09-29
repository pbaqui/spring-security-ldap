package py.com.kalpa.springsecurityldap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import py.com.kalpa.springsecurityldap.domain.TipoTarea;

public interface TipoTareaRepository extends JpaRepository<TipoTarea, Long> {

}
