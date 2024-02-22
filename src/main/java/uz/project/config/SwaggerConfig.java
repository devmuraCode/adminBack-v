package uz.project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GaloreGreenHouse Services",
                version = "1.0",
                contact = @Contact(
                        name = "GaloreGreenHouse SUPPORT", email = "islombekqurbonovfd@gmail.com"
                ),
                description = "GaloreGreenHouse Resources"
        ),
        servers = {
                @Server(url = "http://localhost:${server.port}", description = "Local development")
        }
)
@SecurityScheme(
        name = SwaggerConfig.BEARER,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Slf4j
public class SwaggerConfig {

    public static final String BEARER = "Bearer Authentication";

    @Bean
    public CorsFilter corsFilter() {
        var source = new UrlBasedCorsConfigurationSource();
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        source.registerCorsConfiguration("/api/v1/**", configuration);
        source.registerCorsConfiguration("/management/**", configuration);
        source.registerCorsConfiguration("/v2/api-docs", configuration);
        return new CorsFilter(source);
    }
}
