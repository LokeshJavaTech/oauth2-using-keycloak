package com.lokesh.oauth2_using_keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity        // If application is build using Spring Reactive Module. Ex: Spring Cloud Gateway project, which is used to create API Gateway
public class SecurityConfig
{
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity)
    {
        serverHttpSecurity
            .authorizeExchange(
                    exchanges -> exchanges.pathMatchers(HttpMethod.GET)
                                              .permitAll()
                                          .pathMatchers("/api/user/**")
                                              .authenticated()
            )
            .oauth2ResourceServer(
                    spec -> spec.jwt(Customizer.withDefaults())
            );
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }
}
