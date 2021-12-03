package com.ynero.ss.execution.security;

import com.ynero.ss.execution.persistence.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("authService")
public class AuthorizationComponent {

    private final UserRepository repository;

    public AuthorizationComponent(UserRepository repository) {
        this.repository = repository;
    }

    public boolean mayGetNodes(UserDetails principal, String tenantId) {
        if (!principal.getAuthorities().contains(new SimpleGrantedAuthority("data.nodes.read")))
            return false;
        var username = principal.getUsername();
        var user = repository.findByUsername(username);
        var usersTenantId = user.getTenantId();
        return usersTenantId.equals(tenantId);
    }

    public boolean mayCreateNodes(UserDetails principal, String tenantId) {
        if (!principal.getAuthorities().contains(new SimpleGrantedAuthority("data.nodes.read")))
            return false;
        var username = principal.getUsername();
        var user = repository.findByUsername(username);
        var usersTenantId = user.getTenantId();
        return usersTenantId.equals(tenantId);
    }
}
