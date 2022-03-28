package com.ldap.ad.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
	
	@JsonProperty("message")	
	private String message;
	
	@JsonProperty("data")	
	private T data;
}
