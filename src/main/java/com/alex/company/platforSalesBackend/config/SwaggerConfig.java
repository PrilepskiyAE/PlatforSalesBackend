package com.alex.company.platforSalesBackend.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Полное API приложение",
                version = "2.0.0",
                description = "Автоматически сгенерированная документация со всеми эндпоинтами"
        )
)
public class SwaggerConfig {

    @Bean
    public OpenAPI fullOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Полное API приложение")
                        .version("2.0.0")
                        .description("Автоматически сгенерированная документация со всеми эндпоинтами\n\n" +
                                "**Документация API** доступна в формате OpenAPI 3.0 в корне проекта (файл `openapi.yaml`)")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")
                        )
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("API Support")
                                .email("support@example.com")
                                .url("https://example.com/support")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )

                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi internalApi() {
        return GroupedOpenApi.builder()
                .group("internal")
                .pathsToMatch("/api/**")
                .pathsToExclude("/api/auth/**")
                .build();
    }


}
