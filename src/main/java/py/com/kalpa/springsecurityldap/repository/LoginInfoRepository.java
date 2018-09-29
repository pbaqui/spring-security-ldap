package py.com.kalpa.springsecurityldap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.kalpa.springsecurityldap.domain.LoginInfo;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

}
