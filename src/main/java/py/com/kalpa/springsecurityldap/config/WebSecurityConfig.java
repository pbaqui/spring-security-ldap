package py.com.kalpa.springsecurityldap.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

import py.com.kalpa.springsecurityldap.repository.RoleRepository;
import py.com.kalpa.springsecurityldap.seguridad.CustomActiveDirectoryLdapAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled =true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private RoleRepository roleRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.anyRequest().authenticated()
		.and().formLogin().permitAll()
		.and().logout().permitAll();//.and().exceptionHandling().accessDeniedPage("/error_403");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) 
			throws Exception {
		authManagerBuilder.authenticationProvider
		(activeDirectoryLdapAuthenticationProvider())
				.userDetailsService(userDetailsService());
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays
				.asList(activeDirectoryLdapAuthenticationProvider()));
	}

	@Bean
	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		CustomActiveDirectoryLdapAuthenticationProvider provider 
		= new CustomActiveDirectoryLdapAuthenticationProvider();
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		provider.setRoleRepository(roleRepository);
		return provider;
	}
	
	@Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Elimina el prefijo ROLE_
    }
}
