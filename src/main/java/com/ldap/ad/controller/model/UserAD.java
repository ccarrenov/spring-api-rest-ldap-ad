package com.ldap.ad.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAD {
	
	@JsonProperty("displayName")		
	private String displayName;
	
	@JsonProperty("givenname")	
	private String givenname;
	
	@JsonProperty("cn")	
	private String cn;
	
	@JsonProperty("sn")	
	private String sn;
	
	@JsonProperty("user")	
	private String user;
	
	@JsonProperty("userPrincipalName")	
	private String userPrincipalName;
}
