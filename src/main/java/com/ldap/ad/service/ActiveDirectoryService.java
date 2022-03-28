package com.ldap.ad.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ldap.ad.controller.model.Response;
import com.ldap.ad.util.ActiveDirectoryUtil;

@Service
public class ActiveDirectoryService {

	@Value("${ad.server.provider}")
	private String serverProvider;
	@Value("${ad.port.provider}")
	private String portProvider;
	@Value("${ad.domain.component.one}")
	private String domainComponentOne;
	@Value("${ad.domain.component.two}")
	private String domainComponentTwo;
	@Value("${ad.domain.name}")
	private String domainName;

	public ResponseEntity<Response<Object>> authentication(String userName, String credential) {

		ActiveDirectoryUtil ad = new ActiveDirectoryUtil(serverProvider, portProvider, domainComponentOne,
				domainComponentTwo, domainName);
		return ad.authorization(userName, credential);
	}
	
}
