package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface NodeService {
    List<NodeGetDTO> getAllUsersNodes(String username);
    List<NodeGetDTO> getAllTenantsNodes(String tenantId);
    boolean update(NodeDTO dto, String nodeId);
    boolean delete(String nodeId);
    String save(NodeDTO dto);
    NodeGetDTO findById(String nodeId);
}
