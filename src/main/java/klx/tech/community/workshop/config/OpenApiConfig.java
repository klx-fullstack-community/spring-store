package klx.tech.community.workshop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Full Stack K-Tech Community",
            email = "orlando.santos@klx.com",
            url = "https://www.klx.pt/"
        ),
        description = "Spring Boot backend for store application",
        title = "Store Backend Server",
        version = "1.0.0"
    ),
    servers = 
        @Server(
            description = "Local Environment",
            url = "http://localhost:8080"
        )
)
public class OpenApiConfig {

}
