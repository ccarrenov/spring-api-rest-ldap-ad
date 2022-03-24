package com.ldap.ad.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ldap.ad.App;
import com.ldap.ad.service.ActiveDirectoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping("/app/v1/active-directory")
@Api(value = "Eleccion Flujo", tags = { App.ACTIVE_DIRECTORY })
public class ActiveDirectoryController {

	private static final Logger LOGGER = Logger.getLogger(ActiveDirectoryController.class.getName());
	
	@Autowired
	private ActiveDirectoryService adService;

	@ApiOperation(value = "authentication", tags = { App.ACTIVE_DIRECTORY })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 500, message = "Failure") })
	@GetMapping(value = "authentication/{userName}/{credential}", consumes = {}, produces = {
			MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> authentication(@PathVariable String userName, @PathVariable String credential) {
		LOGGER.info("userName: " + userName);
		LOGGER.info("credential: " + credential);
		return adService.authentication(userName, credential);
	}

	public ResponseEntity<String> buildResponse(String type, String msg, HttpStatus status) {
		LOGGER.info(type + " : " + msg);
		return new ResponseEntity<>(msg, status);
	}
}
