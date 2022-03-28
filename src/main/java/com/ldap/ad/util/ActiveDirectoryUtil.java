package com.ldap.ad.util;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.ldap.ad.controller.model.Response;
import com.ldap.ad.controller.model.UserAD;

public class ActiveDirectoryUtil {

	private static final Logger LOGGER = Logger.getLogger(ActiveDirectoryUtil.class.getName());
	private static final String INITIAL_CONTEXT_FACTORY_AD = "com.sun.jndi.ldap.LdapCtxFactory";
	private static final String MSG_ERROR = "error ";

	private String serverProvider;
	private String portProvider;
	private String domainComponentOne;
	private String domainComponentTwo;
	private String domainName;
	private Hashtable<String, String> env;

	public ActiveDirectoryUtil(String serverProvider, String portProvider, String domainComponentOne,
			String domainComponentTwo, String domainName) {
		this.serverProvider = serverProvider;
		this.portProvider = portProvider;
		this.domainComponentOne = domainComponentOne;
		this.domainComponentTwo = domainComponentTwo;
		this.domainName = domainName;
	}

	private void init(String user, String credential) {
		env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY_AD);
		env.put(Context.PROVIDER_URL, "ldap://" + serverProvider + ":" + portProvider + "/dc=" + domainComponentOne
				+ ",dc=" + domainComponentTwo);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, domainName + "\\" + user);
		env.put(Context.SECURITY_CREDENTIALS, credential);
	}

	public ResponseEntity<Response<Object>> authorization(String user, String credential) {
		LOGGER.info("INIT authorization");
		init(user, credential);
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(env);
			UserAD userAD = getData(ctx, user);
			return buildResponse("success", "autorizado.", HttpStatus.OK, userAD, MediaType.APPLICATION_JSON);
		} catch (AuthenticationException e) {
			return buildResponse(MSG_ERROR, "usuario o contraseña incorrectas.", HttpStatus.UNAUTHORIZED, null, MediaType.APPLICATION_JSON);
		} catch (NamingException e) {
			return buildResponse(MSG_ERROR, "Error AD: nombre incorrecto.", HttpStatus.UNAUTHORIZED, null, MediaType.APPLICATION_JSON);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return buildResponse(MSG_ERROR, "Error en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR, null, MediaType.APPLICATION_JSON);
		} finally {
			close(ctx);
			LOGGER.info("FINISH authorization");
		}
	}

	@SuppressWarnings("rawtypes")
	public UserAD getData(DirContext ctx, String user) throws Exception {
		UserAD userAD = null;
		NamingEnumeration answer = null;
		try {
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SUBTREE_SCOPE);
			answer = ctx.search(env.get(Context.PROVIDER_URL), "(objectClass=user)", controls);
			while (answer.hasMore()) {
				Attributes attr = ((SearchResult) answer.next()).getAttributes();
				Attribute userPrincipalName = attr.get("userPrincipalName");
				if (userPrincipalName != null && userPrincipalName.toString().contains(user)) {
					String displayName = (attr.get("displayName") != null) ? attr.get("displayName").toString() : "";
					String givenname = (attr.get("givenname") != null) ? attr.get("givenname").toString() : "";
					String sn = (attr.get("sn") != null) ? attr.get("sn").toString() : "";
					String cn = (attr.get("cn") != null) ? attr.get("cn").toString() : "";
					LOGGER.info("displayName -> " + displayName);
					LOGGER.info("givenname -> " + givenname);
					LOGGER.info("sn -> " + sn);
					LOGGER.info("userPrincipalName -> " + userPrincipalName);
					userAD = new UserAD(displayName, givenname, cn, sn, user, user);
				}
			}
		} catch(PartialResultException p) {
			LOGGER.error("Existen más referencias no listadas.");
		} finally {
			close(ctx);
			close(answer);
		}

		return userAD;
	}

	public ResponseEntity<Response<Object>> buildResponse(String type, String msg, HttpStatus status, Object obj, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        
		LOGGER.info(type + " : " + msg);
		Response<Object> resp = new Response<>(msg, obj);
		return new ResponseEntity<>(resp, headers, status);
	}

	public <T> void close(T obj) {
		LOGGER.info("close -> INIT");
		if (obj != null) {
			try {
				if (obj instanceof Context) {
					((Context) obj).close();
				} else if (obj instanceof NamingEnumeration) {
					((NamingEnumeration<?>) obj).close();
				}
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			LOGGER.info("object null.");
		}
	}
}
