package com.ldap.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
@EnableScheduling
public class App {

    public static final String ACTIVE_DIRECTORY= "ACTIVE DIRECTORY";
    public static final String ACTIVE_DIRECTORY_DESCRIPTION= "WEB SERVICE WITH ALL ACTIVE DIRECTORY (AD)";

    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(Predicates.not(PathSelectors.regex("/error.*"))).build().enableUrlTemplating(false)
                .tags(new Tag(ACTIVE_DIRECTORY, ACTIVE_DIRECTORY_DESCRIPTION));
    }

}
