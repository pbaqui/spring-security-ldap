package py.com.kalpa.springsecurityldap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.kalpa.springsecurityldap.domain.Permiso;
import py.com.kalpa.springsecurityldap.domain.Role;
import py.com.kalpa.springsecurityldap.domain.RoleId;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

	@Query("SELECT r.id.role FROM Role r where r.id.username = :username")
	List<String> buscarRolesPorUsuario(@Param("username") String username);

	@Query("SELECT r.permisos FROM Role r where r.id.username = :username")
	List<Permiso> buscarPermisosPorUsuario(@Param("username") String username);
	

	@Query("SELECT r FROM Role r where r.id.role = :role")
	Role buscarPorRole(@Param("role") String role);

}
