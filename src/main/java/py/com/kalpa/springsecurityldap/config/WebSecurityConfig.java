package py.com.kalpa.springsecurityldap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.anyRequest().fullyAuthenticated()
		.and().formLogin()
		.and().logout();
	}

	@Override
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userSearchFilter(env.getProperty("ldap.userFilter"))
		.contextSource(contextSource());
	}
	
	@Bean
	public BaseLdapPathContextSource contextSource() throws Exception {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
				env.getProperty("ldap.url"));
		contextSource.setUserDn(env.getProperty("ldap.masterUser"));
		contextSource.setPassword(env.getProperty("ldap.masterUserPassword"));
		contextSource.afterPropertiesSet();
		return contextSource;
	}
}
