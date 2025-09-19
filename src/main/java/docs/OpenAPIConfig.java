package docs;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
        title = "API de Produtos e Simulação de Empréstimo",
        version = "1.0.0",
        description = "API para gerenciamento de produtos financeiros e simulação de empréstimos",
        contact = @Contact(
            name = "Fernando Rosendo",
            email = " "
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor de Teste Local")
    }
)
public class OpenAPIConfig extends Application {
}