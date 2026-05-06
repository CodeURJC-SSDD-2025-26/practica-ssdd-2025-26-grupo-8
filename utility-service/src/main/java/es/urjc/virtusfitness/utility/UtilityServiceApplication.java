package es.urjc.virtusfitness.utility;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info =
        @Info(
            title = "Virtus Fitness - Utility Service API",
            version = "1.0.0",
            description =
                "Stateless utility microservice. Generates PDFs and (in the future) sends emails "
                    + "on behalf of app-service. No persistence, no authentication: receives all "
                    + "the data it needs in each request body.",
            contact = @Contact(name = "Grupo 8 - SSDD URJC"),
            license = @License(name = "MIT")),
    servers = {@Server(url = "http://localhost:8080", description = "Local")})
public class UtilityServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UtilityServiceApplication.class, args);
  }
}
