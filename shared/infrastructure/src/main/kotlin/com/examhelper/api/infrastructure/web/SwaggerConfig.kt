package com.examhelper.api.infrastructure.web

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val securitySchemeName = "BearerAuth"

        return OpenAPI()
            .info(apiInfo())
            .servers(servers(activeProfile = "dev"))
            /*
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT 인증 토큰. 'Bearer {token}' 형식")
                )
            )
            */
    }

    private fun apiInfo() = Info()
        .title("PSAT Question Generation API")
        .description("""
            ## 5급 PSAT 문제 생성 및 CBT 서비스
            
            RAG(Retrieval-Augmented Generation) 기반으로 10년치 기출 데이터에서  
            논리 프레임을 추출하여 고품질 모의고사 문제를 생성합니다.
        """.trimIndent())
        .version("v1.0.0")
        .license(
            License()
                .name("Private")
        )

    private fun servers(activeProfile: String) = when (activeProfile) {
        "prod" -> listOf(
            Server().url("https://api.yourapp.com").description("Production"),
            Server().url("http://localhost:8080").description("Local"),
        )
        else -> listOf(
            Server().url("http://localhost:8080").description("Local"),
        )
    }
}
