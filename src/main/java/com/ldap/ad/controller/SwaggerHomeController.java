package com.ldap.ad.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("")
@ApiIgnore
public class SwaggerHomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerHomeController.class);

	@GetMapping(path = "")
	public String swaggerUI() {
		LOGGER.info("Redirect swagger home");
		return "redirect:/swagger-ui.html";
	}
}
