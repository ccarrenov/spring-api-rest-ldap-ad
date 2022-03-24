package com.ldap.ad.util;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ActiveDirectoryUtil {

	private static final Logger LOGGER = Logger.getLogger(ActiveDirectoryUtil.class.getName());
	private static final String INITIAL_CONTEXT_FACTORY_AD = "com.sun.jndi.ldap.LdapCtxFactory";

	private String serverProvider;
	private String portProvider;
	private String domainComponentOne;
	private String domainComponentTwo;
	private String domainName;
	private Hashtable<String, String> env;
	
	public ActiveDirectoryUtil(String serverProvider, String portProvider, String domainComponentOne, String domainComponentTwo, String domainName) {
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
	
	public ResponseEntity<String> authorization(String user, String credential) {
		LOGGER.info("INIT authorization");		
		init(user, credential);
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(env);
			return buildResponse("success", "autorizado.", HttpStatus.OK);
		} catch (AuthenticationException e) {
			return buildResponse("error", "usuario o contraseña incorrectas.", HttpStatus.UNAUTHORIZED);
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			return buildResponse("error", "Error en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			closeContext(ctx);
			LOGGER.info("FINISH authorization");
		}			
	}
	
	public ResponseEntity<String> buildResponse(String type, String msg, HttpStatus status) {
		LOGGER.info(type + " : " + msg);
		return new ResponseEntity<>(msg, status);
	}
	
	public void closeContext(DirContext ctx) {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);					
			}
		}
	}
}
