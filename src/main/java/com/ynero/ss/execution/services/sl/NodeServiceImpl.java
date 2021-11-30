package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class NodeServiceImpl implements NodeService {

    @Setter(onMethod_ = {@Autowired})
        private NodeCacheService cacheService;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    @Override
    public List<NodeGetDTO> getAllUsersNodes(String username) {
        var dtoList = cacheService.getAllUsersNodes(username)
                .stream().map( node -> modelMapper.map(node, NodeGetDTO.class))
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<NodeGetDTO> getAllTenantsNodes(String tenantId) {
        var dtoList = cacheService.getAllTenantsNodes(tenantId)
                .stream().map( node -> modelMapper.map(node, NodeGetDTO.class))
                .collect(Collectors.toList());
        return dtoList;
    }

    public boolean update(NodeDTO dto, String nodeId) {
        var node = modelMapper.map(dto, Node.class);
        node.setNodeId(UUID.fromString(nodeId));
        var result = cacheService.update(node);
        return result;
    }

    public boolean delete(String nodeId) {
        var id = UUID.fromString(nodeId);
        var result = cacheService.delete(id);
        return result;
    }

    public String save(NodeDTO dto) {
        var node = modelMapper.map(dto, Node.class);
        if (node.getNodeId() == null) {
            node.setNodeId(UUID.randomUUID());
        }
        node = cacheService.save(node);
        return node.getNodeId().toString();
    }

    @SneakyThrows
    public NodeGetDTO findById(String nodeId) {
        var id = UUID.fromString(nodeId);
        var node = cacheService.findByNodeId(id);
        log.info("node: {}", node);
        if (!node.isEmpty()) {
            var nodeDTO = modelMapper.map(node.get(), NodeGetDTO.class);
            return nodeDTO;
        }
        throw new Exception("No such node");
    }
}
