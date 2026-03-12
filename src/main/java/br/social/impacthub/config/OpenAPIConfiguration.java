package br.social.impacthub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * This class sets up the OpenAPI specification for the application, including server information,
 * API metadata, and security schemes (Bearer Token).
 * @author Filipe Martins
 */
@Configuration
public class OpenAPIConfiguration {
    private String title = "Impact Hub";

    private String version = "1.0.0";

    @Value("${spring.application.url}")
    private String url;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url(url)
                        )
                )
                .info(new Info()
                        .title(title)
                        .version(version)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")); // <-- global requirement
    }
}
