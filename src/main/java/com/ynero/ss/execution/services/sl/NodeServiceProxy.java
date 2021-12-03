package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface NodeServiceProxy {

    @PreAuthorize("@authService.mayGetUsersNodes(principal)")
    List<NodeGetDTO> getAllUsersNodes(Authentication authentication);

    @PreAuthorize("@authService.mayGetNodes(principal, #tenantId)")
    List<NodeGetDTO> getAllTenantsNodes(String tenantId);

    @PreAuthorize("@authService.mayUpdateNodes(principal, #dto.tenantId)")
    boolean update(NodeDTO dto, String nodeId, Authentication authentication);

  /*  @PreAuthorize("@authService.mayUpdateNodes(principal, #nodeId)")*/
    boolean delete(String nodeId, Authentication authentication);

    @PreAuthorize("@authService.maySaveNodes(principal, #dto.tenantId)")
    String save(NodeDTO dto, Authentication authentication);

    @PreAuthorize("@authService.mayFindNodes(principal, #nodeId)")
    NodeGetDTO findById(String nodeId, Authentication authentication);
}
