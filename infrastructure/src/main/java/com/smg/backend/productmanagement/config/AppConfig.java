package com.smg.backend.productmanagement.config;

import com.smg.backend.productmanagement.core.port.ProductService;
import com.smg.backend.productmanagement.core.usecase.CreateProductUseCase;
import com.smg.backend.productmanagement.core.usecase.CreateProductUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableR2dbcAuditing
@EnableAsync
//@EnableCaching
//@EnableRetry
@Configuration
public class AppConfig {

    private static final String[] EXCLUDED_URLS = new String[] {
            // for Swagger UI v2
            "/v2/api-docs",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",

            // for Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",

            // products
            "/products/**"
    };

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers(EXCLUDED_URLS).permitAll()
                                .anyExchange().authenticated()
                )
                .csrf().disable()
                .build();
    }

    @Bean
    CreateProductUseCase createProductUseCase(ProductService productService) {
        return new CreateProductUseCaseImpl(productService);
    }

}
