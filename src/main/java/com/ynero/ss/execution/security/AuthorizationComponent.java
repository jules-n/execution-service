package com.ynero.ss.execution.security;

import com.ynero.ss.execution.persistence.user.UserRepository;
import com.ynero.ss.execution.services.sl.NodeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("authService")
public class AuthorizationComponent {

    private final UserRepository userRepository;
    private final NodeService nodeService;

    public AuthorizationComponent(UserRepository userRepository, NodeService nodeService) {
        this.userRepository = userRepository;
        this.nodeService = nodeService;
    }

    public boolean mayGetTenantsNodes(Authentication authentication, String tenantId) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("data.nodes.read")))
            return false;
        var username = ((UserDetails)authentication.getPrincipal()).getUsername();
        var user = userRepository.findByUsername(username);
        var usersTenantId = user.getTenantId();
        return usersTenantId.equals(tenantId);
    }

    public boolean mayGetNodes(Authentication authentication) {
        return  authentication.getAuthorities().contains(new SimpleGrantedAuthority("data.nodes.read"));
    }

    public boolean mayCreateNodes(Authentication authentication, String tenantId) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("data.nodes.create")))
            return false;
        var username = ((UserDetails)authentication.getPrincipal()).getUsername();
        var user = userRepository.findByUsername(username);
        var usersTenantId = user.getTenantId();
        return usersTenantId.equals(tenantId);
    }

    public boolean mayDeleteNodes(Authentication authentication, String nodeId) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("data.nodes.delete")))
            return false;
        var node = nodeService.findById(nodeId);
        var username = ((UserDetails)authentication.getPrincipal()).getUsername();
        var user = userRepository.findByUsername(username);
        return node.getTenantId().equals(user.getTenantId());
    }

    public boolean mayUpdateNodes(Authentication authentication, String nodeId) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("data.nodes.update")))
            return false;
        var node = nodeService.findById(nodeId);
        var username = ((UserDetails)authentication.getPrincipal()).getUsername();
        var user = userRepository.findByUsername(username);
        return node.getTenantId().equals(user.getTenantId());
    }
}
