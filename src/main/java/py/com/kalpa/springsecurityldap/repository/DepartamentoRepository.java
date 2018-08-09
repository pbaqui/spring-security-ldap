package py.com.kalpa.springsecurityldap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.kalpa.springsecurityldap.domain.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

}
