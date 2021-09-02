package com.ynero.ss.execution.persistence.node;

import com.ynero.ss.execution.domain.Node;

import java.util.UUID;

public interface NodeRepositoryCustom{
    boolean update(Node node);
    boolean delete(UUID nodeId);
}
