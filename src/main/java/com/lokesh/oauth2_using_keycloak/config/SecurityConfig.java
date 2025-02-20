package com.lokesh.oauth2_using_keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

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
                                              .hasRole("User_CallCenter_Role")
            )
            .oauth2ResourceServer(
                    spec -> spec.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtToGrantedAuthoritiesConverter()))
            );
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtToGrantedAuthoritiesConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
