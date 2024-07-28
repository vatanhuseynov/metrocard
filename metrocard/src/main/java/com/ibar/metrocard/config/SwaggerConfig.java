package com.ibar.metrocard.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI publicApi() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
            .components(
                    new Components()
                            .addSecuritySchemes(securitySchemeName,
                                    new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                            )
            )
            .security(List.of(new SecurityRequirement().addList(securitySchemeName)))
            .info(
                    new Info()
                            .title("METRO CARD API")
                            .description("APİ Of Resource")
                            .version("1.0.0")
                            .license(
                                    new License()
                                            .name("Apache License 2.0")
                                            .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .externalDocs(
                    new ExternalDocumentation().description("IBAR").url("https://abb-bank.az/"));
  }
}
