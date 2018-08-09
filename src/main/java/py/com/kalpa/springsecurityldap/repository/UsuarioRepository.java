package py.com.kalpa.springsecurityldap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.kalpa.springsecurityldap.domain.Cargo;
import py.com.kalpa.springsecurityldap.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

}
