package com.ynero.ss.excecution.services.bl;

import com.ynero.ss.excecution.domain.Node;
import com.ynero.ss.excecution.domain.dto.NodeDTO;

import java.util.Optional;
import java.util.UUID;

public interface NodeService {

    boolean update(NodeDTO dto, String nodeId);
    boolean delete(String nodeId);
    UUID save(NodeDTO dto);
    Optional<Node> findById(String nodeId);
}
