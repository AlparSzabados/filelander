package org.filelander.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {

    @Value("${swagger.host.addr}")
    private String hostAddress;

    @Value("${swagger.host.port}")
    private String hostPort;

    @Value("${spring.profiles.active}")
    private String springProfile;

    @Value("#{systemEnvironment['HOST_NAME']}")
    private String hostName;

    private static final String TITLE = "Filelander API Documentation";
    private static final String DESCRIPTION = "API documentation Filelander service";
    private static final String VERSION = "1.0.0";

    @Bean
    public Docket api(ServletContext servletContext) {
        log.info("Swagger resources host = " + getHostName());

        ApiInfo info = new ApiInfoBuilder().title(TITLE).description(DESCRIPTION).version(VERSION).build();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(info)
                .host(getHostName())
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    private String getHostName() {
        String localHostName = null;
        if (springProfile.equals("local")) {
            localHostName = hostAddress + ":" + hostPort;
        } else {
            return hostName;
        }

        return localHostName;
    }

	
}
