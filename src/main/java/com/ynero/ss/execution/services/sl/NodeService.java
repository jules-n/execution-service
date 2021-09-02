package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;

public interface NodeService {

    boolean update(NodeDTO dto, String nodeId);
    boolean delete(String nodeId);
    String save(NodeDTO dto);
    NodeGetDTO findById(String nodeId);
}
