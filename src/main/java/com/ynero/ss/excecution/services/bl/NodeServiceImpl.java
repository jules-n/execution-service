package com.ynero.ss.excecution.services.bl;

import com.ynero.ss.excecution.domain.Node;
import com.ynero.ss.excecution.domain.dto.NodeDTO;
import com.ynero.ss.excecution.persistence.NodeRepository;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NodeServiceImpl implements NodeService{

    @Setter(onMethod_ = {@Autowired})
    private NodeRepository nodeRepository;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    public boolean update(NodeDTO dto, String nodeId) {
        var node = modelMapper.map(dto, Node.class);
        node.setNodeId(UUID.fromString(nodeId));
        var result = nodeRepository.update(node);
        return result;
    }

    public boolean delete(String nodeId) {
      var id = UUID.fromString(nodeId);
      var result =  nodeRepository.delete(id);
      return result;
    }

    public UUID save(NodeDTO dto) {
        var node = modelMapper.map(dto, Node.class);
        node.setNodeId(UUID.randomUUID());
        node = nodeRepository.save(node);
        return node.getNodeId();
    }

    public Optional<Node> findById(String nodeId) {
        var id = UUID.fromString(nodeId);
        return nodeRepository.findByNodeId(id);
    }
}
