package py.com.kalpa.springsecurityldap.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import py.com.kalpa.springsecurityldap.config.seguridad.CustomActiveDirectoryLdapAuthenticationProvider;
import py.com.kalpa.springsecurityldap.repository.RoleRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private RoleRepository roleRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expression = http
				.authorizeRequests();

		http.exceptionHandling().authenticationEntryPoint(getAuthenticationEntryPoint());
		http.requestCache().disable();
		expression.antMatchers(HttpMethod.GET, "/sesion/info").permitAll();
		expression.antMatchers(HttpMethod.GET, "/cerrar_sesion").permitAll();
		expression.antMatchers(HttpMethod.GET, "/usuarios/**").hasAuthority("Usuario_sel");
		expression.antMatchers(HttpMethod.GET, "/cargos/**").hasAuthority("Cargo_sel");
		expression.antMatchers(HttpMethod.POST, "/cargos/**").hasAuthority("Cargo_ins");
		expression.antMatchers(HttpMethod.DELETE, "/cargos/**").hasAuthority("Cargo_del");

		expression.antMatchers(HttpMethod.GET, "/tareas/**").hasAuthority("Tarea_sel");
		expression.antMatchers(HttpMethod.GET, "/solicitantes/**").permitAll();
		expression.antMatchers(HttpMethod.POST, "/solicitantes/**").permitAll();
		expression.antMatchers(HttpMethod.DELETE, "/solicitantes/**").permitAll();
		http.cors().disable();
		//desabilitar Cross-site request forgery
		http.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.authenticationProvider(activeDirectoryLdapAuthenticationProvider())
				.userDetailsService(userDetailsService());
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
	}

	// Retornar 401 cuando usuario no esta autenticado
	private AuthenticationEntryPoint getAuthenticationEntryPoint() {

		return new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {

				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		};
	}

	@Bean
	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		CustomActiveDirectoryLdapAuthenticationProvider provider = new py.com.kalpa.springsecurityldap.config.seguridad.CustomActiveDirectoryLdapAuthenticationProvider();
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
