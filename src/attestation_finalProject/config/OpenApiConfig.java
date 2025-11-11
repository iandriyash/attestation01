package attestation_finalProject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pizzeriaOpenAPI() {
        // объявляем basicAuth как обязательную схему безопасности по умолчанию
        return new OpenAPI()
                .info(new Info()
                        .title("Pizzeria API")
                        .description("Серверное API для приложения «Пиццерия» (итоговая аттестация)")
                        .version("v1"))
                .components(new Components().addSecuritySchemes("basicAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }
}
