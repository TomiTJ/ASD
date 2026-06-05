package com.asd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankAdminOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Admin API")
                        .description("""
                                REST API for the Bank Admin system.
                                Provides endpoints for managing accounts, transactions,
                                loan applications, users, and dashboard metrics.

                                **Authentication:** Session-based. Log in at `/login` before calling protected endpoints.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bank Admin Team")
                                .email("admin@bank.local"))
                        .license(new License().name("Private")))
                .servers(List.of(
                        new Server().url("/").description("Current server")
                ));
    }
}
