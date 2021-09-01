package com.ynero.ss.excecution.persistence;

import com.ynero.ss.excecution.domain.Node;

import java.util.UUID;

public interface NodeRepositoryCustom{
    boolean update(Node node);
    boolean delete(UUID nodeId);
}
