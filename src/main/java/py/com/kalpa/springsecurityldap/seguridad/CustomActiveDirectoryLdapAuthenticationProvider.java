package py.com.kalpa.springsecurityldap.seguridad;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.util.StringUtils;

import py.com.kalpa.springsecurityldap.domain.Permiso;
import py.com.kalpa.springsecurityldap.repository.RoleRepository;

public final class CustomActiveDirectoryLdapAuthenticationProvider extends AbstractLdapAuthenticationProvider {

	private static Logger log = LoggerFactory.getLogger(CustomActiveDirectoryLdapAuthenticationProvider.class);

	@Autowired
	private RoleRepository roleRepository;

	private static final Pattern SUB_ERROR_CODE = Pattern.compile(".*data\\s([0-9a-f]{3,4}).*");

	private static final int USERNAME_NOT_FOUND = 0x525;
	private static final int INVALID_PASSWORD = 0x52e;
	private static final int NOT_PERMITTED = 0x530;
	private static final int PASSWORD_EXPIRED = 0x532;
	private static final int ACCOUNT_DISABLED = 0x533;
	private static final int ACCOUNT_EXPIRED = 0x701;
	private static final int PASSWORD_NEEDS_RESET = 0x773;
	private static final int ACCOUNT_LOCKED = 0x775;

	@Value("${ldap.domain}")
	private String domain;

	@Value("${ldap.url}")
	private String url;

	@Value("${ldap.rootDn}")
	private String rootDn;

	@Value("${ldap.userFilter}")
	private String searchFilter;

	private boolean convertSubErrorCodesToExceptions;

	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	ContextFactory contextFactory = new ContextFactory();

	public CustomActiveDirectoryLdapAuthenticationProvider() {
	}

	@Override
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
		String username = auth.getName();
		String password = (String) auth.getCredentials();

		DirContext ctx = bindAsUser(username, password);

		try {
			return searchForUser(ctx, username);
		} catch (NamingException e) {
			logger.error("Error al encontrar el directorio para el usuario autenticado " + username, e);
			throw badCredentials(e);
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}

	@Override
	protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username,
			String password) {
		System.out.println("username: " + username);
		List<GrantedAuthority> authorities = new ArrayList();
		List<String> roles = roleRepository.buscarRolesPorUsuario(username);
		List<Permiso> privilegios = roleRepository.buscarPermisosPorUsuario(username);

		// Los roles en la base de datos deben tener el prefijo 'ROLE_' o se lo agrega
		String DEFAULT_ROLE_PREFIX = "ROLE_";

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE_PREFIX + role));
		}
		for (Permiso privilegio : privilegios) {
			authorities.add(new SimpleGrantedAuthority(privilegio.getNombre()));
		}
		this.log.info("Usuario [" + username + "] Roles " + Arrays.asList(authorities));
		return authorities;
	}
	
	private String rootDnFromDomain(String domain) {
		String[] tokens = StringUtils.tokenizeToStringArray(domain, ".");
		StringBuilder root = new StringBuilder();
		for (String token : tokens) {
			if (root.length() > 0) {
				root.append(',');
			}
			root.append("dc=").append(token);
		}
		return root.toString();
	}

	private DirContext bindAsUser(String username, String password) {
		final String bindUrl = url;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		String bindPrincipal = createBindPrincipal(username);
		env.put(Context.SECURITY_PRINCIPAL, bindPrincipal);
		env.put(Context.PROVIDER_URL, bindUrl);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.OBJECT_FACTORIES, DefaultDirObjectFactory.class.getName());

		try {
			return contextFactory.createContext(env);
		} catch (NamingException e) {
			if ((e instanceof AuthenticationException) || (e instanceof OperationNotSupportedException)) {
				handleBindException(bindPrincipal, e);
				throw badCredentials(e);
			} else {
				throw LdapUtils.convertLdapException(e);
			}
		}
	}

	private void handleBindException(String bindPrincipal, NamingException exception) {
		if (logger.isDebugEnabled()) {
			logger.debug("Autenticacion para " + bindPrincipal + " fallo:" + exception);
		}
		int subErrorCode = parseSubErrorCode(exception.getMessage());
		if (subErrorCode <= 0) {
			logger.debug("Error al localizar codigo de sub-error AD");
			return;
		}
		logger.info("Fallo la autenticacion Active Directory: " + subCodeToLogMessage(subErrorCode));
		if (convertSubErrorCodesToExceptions) {
			raiseExceptionForErrorCode(subErrorCode, exception);
		}
	}

	private BadCredentialsException badCredentials() {
		return new BadCredentialsException(
				messages.getMessage("LdapAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	private BadCredentialsException badCredentials(Throwable cause) {
		return (BadCredentialsException) badCredentials().initCause(cause);
	}

	private DirContextOperations searchForUser(DirContext context, String username) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String bindPrincipal = createBindPrincipal(username);
		String searchRoot = rootDn != null ? rootDn : searchRootFromPrincipal(bindPrincipal);

		try {
			return SpringSecurityLdapTemplate.searchForSingleEntryInternal(context, searchControls, searchRoot,
					searchFilter, new Object[] { bindPrincipal });
		} catch (IncorrectResultSizeDataAccessException incorrectResults) {
			// Search should never return multiple results if properly configured - just
			// rethrow
			if (incorrectResults.getActualSize() != 0) {
				throw incorrectResults;
			}
			// If we found no results, then the username/password did not match
			UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException(
					"Usuario " + username + " no encontrado en directorio.", incorrectResults);
			throw badCredentials(userNameNotFoundException);
		}
	}

	String createBindPrincipal(String username) {
		if (domain == null || username.toLowerCase().endsWith(domain)) {
			return username;
		}

		return username + "@" + domain;
	}

	private int parseSubErrorCode(String message) {
		Matcher m = SUB_ERROR_CODE.matcher(message);
		if (m.matches()) {
			return Integer.parseInt(m.group(1), 16);
		}
		return -1;
	}

	private String subCodeToLogMessage(int code) {
		switch (code) {
		case USERNAME_NOT_FOUND:
			return "Usuario no encontrado";
		case INVALID_PASSWORD:
			return "Password ingresado invalido";
		case NOT_PERMITTED:
			return "Usuario no permitido para el ingreso";
		case PASSWORD_EXPIRED:
			return "Password ha expirado";
		case ACCOUNT_DISABLED:
			return "Cuenta desabilitada";
		case ACCOUNT_EXPIRED:
			return "Cuenta expirada";
		case PASSWORD_NEEDS_RESET:
			return "Usuario necesita restablecer su password";
		case ACCOUNT_LOCKED:
			return "Cuenta bloqueada";
		}
		return "Desconocido (error code " + Integer.toHexString(code) + ")";
	}

	public void setConvertSubErrorCodesToExceptions(boolean convertSubErrorCodesToExceptions) {
		this.convertSubErrorCodesToExceptions = convertSubErrorCodesToExceptions;
	}

	private String searchRootFromPrincipal(String bindPrincipal) {
		int atChar = bindPrincipal.lastIndexOf('@');
		if (atChar < 0) {
			logger.debug("User principal '" + bindPrincipal
					+ "' does not contain the domain, and no domain has been configured");
			throw badCredentials();
		}
		return rootDnFromDomain(bindPrincipal.substring(atChar + 1, bindPrincipal.length()));
	}

	private void raiseExceptionForErrorCode(int code, NamingException exception) {
		Throwable cause = new InvalidParameterException(exception.getMessage());
		switch (code) {
		case PASSWORD_EXPIRED:
			throw new CredentialsExpiredException(messages.getMessage("LdapAuthenticationProvider.credentialsExpired",
					"User credentials have expired"), cause);
		case ACCOUNT_DISABLED:
			throw new DisabledException(messages.getMessage("LdapAuthenticationProvider.disabled", "User is disabled"),
					cause);
		case ACCOUNT_EXPIRED:
			throw new AccountExpiredException(
					messages.getMessage("LdapAuthenticationProvider.expired", "User account has expired"), cause);
		case ACCOUNT_LOCKED:
			throw new LockedException(
					messages.getMessage("LdapAuthenticationProvider.locked", "User account is locked"), cause);
		default:
			throw badCredentials(cause);
		}
	}

	static class ContextFactory {
		DirContext createContext(Hashtable<?, ?> env) throws NamingException {
			return new InitialLdapContext(env, null);
		}
	}

}
