package com.magnus.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.apidoc.customizer.JHipsterOpenApiCustomizer;

@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_API_DOCS)
public class OpenApiConfiguration {

    public static final String API_FIRST_PACKAGE = "com.magnus.web.api";

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI magnusOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Magnus API")
                .description("Magnus Backend API Documentation")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Magnus Team")
                    .email("support@magnus.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .addServersItem(new Server()
                .url(contextPath.isEmpty() ? "/" : contextPath)
                .description("Magnus API Server"))
            .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token for authentication")));
    }

    @Bean
    @ConditionalOnMissingBean(name = "apiFirstGroupedOpenAPI")
    public GroupedOpenApi apiFirstGroupedOpenAPI(
        JHipsterOpenApiCustomizer jhipsterOpenApiCustomizer,
        JHipsterProperties jHipsterProperties
    ) {
        JHipsterProperties.ApiDocs properties = jHipsterProperties.getApiDocs();
        return GroupedOpenApi.builder()
            .group("openapi")
            .addOpenApiCustomizer(jhipsterOpenApiCustomizer)
            .packagesToScan(API_FIRST_PACKAGE)
            .pathsToMatch(properties.getDefaultIncludePattern())
            .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("magnus-public")
            .pathsToMatch("/api/**")
            .pathsToExclude("/api/admin/**")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("magnus-admin")
            .pathsToMatch("/api/admin/**")
            .build();
    }

    @Bean
    public GroupedOpenApi managementApi() {
        return GroupedOpenApi.builder()
            .group("magnus-management")
            .pathsToMatch("/management/**")
            .build();
    }
}
