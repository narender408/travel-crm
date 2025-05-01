package motherson.crm.v3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SpringDocConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                SecurityScheme securityScheme = new SecurityScheme()
                                .type(Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(In.HEADER)
                                .name("Authorization");

                SecurityRequirement globalSecurityRequirement = new SecurityRequirement()
                                .addList("BearerAuth");

                return new OpenAPI()
                                .info(new Info()
                                                .title("motherson Travel CRM V4 API")
                                                .version("4.0.0")
                                                .description("API documentation for motherson"))
                                .addSecurityItem(globalSecurityRequirement)
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("BearerAuth", securityScheme))
                                .addServersItem(new Server()
                                                .url("http://localhost:8080")
                                                .description("Development Server"))
                                .addServersItem(new Server()
                                                .url("http://localhost:8081")
                                                .description("Development Server"))
                                .addServersItem(new Server()
                                                .url("https://apiv4.yourtravelcrm.com")
                                                .description("Production Server"));
        }
}

