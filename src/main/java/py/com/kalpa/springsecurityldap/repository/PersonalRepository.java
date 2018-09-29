package py.com.kalpa.springsecurityldap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.kalpa.springsecurityldap.domain.Personal;

public interface PersonalRepository extends JpaRepository<Personal, Long> {
	// SELECT * FROM personal WHERE codigo = ?
	Personal findByCodigo(String codigo);
}
