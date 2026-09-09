package com.eeluwole.finance_api.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Documents the JWT bearer scheme for Swagger UI's "Authorize" button and
 * sets a proper title/description, so a visitor can log in via
 * /api/v1/auth/login, paste the returned token into Authorize, and then
 * actually exercise the protected endpoints from the docs page itself.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Priscilla Trust Insurance API",
                description = "Backend REST API simulating an insurance and wealth-management platform — "
                        + "accounts, policies, claims, payments, loans, and rewards, all scoped to the client "
                        + "who owns them. Register or log in via the Auth endpoints below, then click "
                        + "Authorize with the returned token to try the rest of the API.",
                version = "v1"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
