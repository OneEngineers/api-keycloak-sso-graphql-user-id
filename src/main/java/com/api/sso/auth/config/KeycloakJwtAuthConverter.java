package com.api.sso.auth.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface KeycloakJwtAuthConverter {
    AbstractAuthenticationToken convert(Jwt jwt);

    default String getPrincipalClaimName(Jwt jwt) {
        String claimName = jwt.getClaimAsString("preferred_username");
        return claimName != null ? claimName : jwt.getSubject();
    }

    default Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Collection<String> roles = Set.of();

        if (realmAccess != null && realmAccess.containsKey("roles")) {
            roles = (Collection<String>) realmAccess.get("roles");
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }
}
