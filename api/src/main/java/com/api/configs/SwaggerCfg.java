package com.api.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerCfg {
    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setServers(
                List.of(new Server().url("http://localhost:8080/"))
        );
        openAPI.setInfo(
                new Info().title("API reference")
        );
        return openAPI;
    }
}
