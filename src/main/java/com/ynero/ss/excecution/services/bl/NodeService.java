package com.ynero.ss.excecution.services.bl;

import com.ynero.ss.excecution.domain.dto.NodeDTO;
import com.ynero.ss.excecution.domain.dto.NodeGetDTO;

import java.util.UUID;

public interface NodeService {

    boolean update(NodeDTO dto, String nodeId);
    boolean delete(String nodeId);
    UUID save(NodeDTO dto);
    NodeGetDTO findById(String nodeId);
}
