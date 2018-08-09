package py.com.kalpa.springsecurityldap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.kalpa.springsecurityldap.domain.Cargo;
import py.com.kalpa.springsecurityldap.domain.Departamento;
import py.com.kalpa.springsecurityldap.domain.Solicitante;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {
	
	// SELECT * FROM solicitante WHERE departamento = ?
	List<Solicitante> findAllByDepartamento(Departamento departamento);

	// SELECT * FROM solicitante WHERE cargo = ?
	List<Solicitante> findAllByCargo(Cargo cargo);
	
	@Query("SELECT s.nombre FROM Solicitante s where s.cedula = :cedula") 
    String findSolcitanteByCedula(@Param("cedula") String cedula);

}
