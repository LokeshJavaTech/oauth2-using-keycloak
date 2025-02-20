package com.lokesh.oauth2_using_keycloak.config;

/*
* This class with extract and convert the ROLES coming inside Access_Token to Spring Security understandable format.
* */

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwtToken)
    {
        System.out.println("Access token Value = "+jwtToken.getTokenValue());
        Map<String, Object> rolesMap = (Map<String, Object>) jwtToken.getClaims().get("realm_access");
        if (CollectionUtils.isEmpty(rolesMap)) {
            return new ArrayList<>();
        }

        // Extract the role from JWT token
        // and append prefix ROLE_ because it is required by spring internally inside .hasRole("role_name") method
        // and Create GrantedAuthority object for Spring Security

        List<String> rolesList = (List<String>) rolesMap.get("roles");
        List<GrantedAuthority> authorities = rolesList.stream()
                                                    .map(roleName -> "ROLE_"+roleName)
                                                    .map(SimpleGrantedAuthority::new)
                                                    .collect(Collectors.toList());
        return authorities;
    }
}
