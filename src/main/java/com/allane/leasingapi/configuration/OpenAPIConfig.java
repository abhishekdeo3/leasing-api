package com.allane.leasingapi.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Allane Leasing API",
                version = "1.0.0",
                description = "This API administrates the Leasing APP. Provides CRUD operation end-point. " +
                        "Main BOs are Customer, Vehicle and Leasing Contract.",
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "x-business-domain", value = "Allane")
                        }),
                        @Extension(properties = {
                                @ExtensionProperty(name = "x-business-objects", parseValue = true, value = "[\"Leasing\"]")
                        }),
                        @Extension(properties = {@ExtensionProperty(name = "x-product", value = "Leasing APP")})
                },
                contact = @Contact(url = "https://allane-mobility-group.com/", name = "Allane Leasing APP", email = "leasing@allane-mobility-group.com")
        ),
        security = {
                @SecurityRequirement(name = "Basic-Authentication"),
        },
        servers = {
                @Server(
                        description = "Allane Leasing API local Environment",
                        url = "http://localhost:8091/"
                )
        }
)

@SecurityScheme(
        name = "Basic-Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@Configuration
public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi groupApiV1() {
        return GroupedOpenApi.builder().group("api-v1").pathsToMatch("/**")
                .build();
    }
}
