package com.ynero.ss.execution.persistence.node;

import com.ynero.ss.execution.domain.Node;

import java.util.List;
import java.util.UUID;

public interface NodeRepositoryCustom{
    boolean update(Node node);
    boolean delete(UUID nodeId);
    List<Node> getAllUsersNodes(String username);
    List<Node> getAllTenantsNodes(String tenantId);
}
