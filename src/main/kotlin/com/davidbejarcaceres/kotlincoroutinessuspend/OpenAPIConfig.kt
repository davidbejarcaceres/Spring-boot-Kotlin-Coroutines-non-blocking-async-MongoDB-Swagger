package com.davidbejarcaceres.kotlincoroutinessuspend

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {
    @Bean
    fun customOpenAPI(@Value("Version: 1.0") appVersion: String?): OpenAPI? {
        return OpenAPI()
                .components(Components().addSecuritySchemes("basicScheme",
                        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .info(Info()
                        .title("Reactive MongoDB non-blocking Kotlin")
                        .description( "Spring Boot API implemented using Reactive an non-blocking IO from MongoDB using Coroutines.")
                        .version(appVersion)
                        .contact( Contact()
                                .name("David Béjar Cáceres")
                                .email("dbc770@inlumine.ual.es")
                                .url("https://www.linkedin.com/in/davidbejarcaceres/")
                        )
                        .license(License().name("GPL 2.0").url("https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html")))
    }
}