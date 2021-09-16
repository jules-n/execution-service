package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.persistence.node.NodeRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Primary
public class NodeCacheService{

    @Setter(onMethod_ = {@Autowired})
    private NodeRepository nodeRepository;

    @Setter(onMethod_ = {@Autowired})
    private NodeCache cache;

    public Optional<Node> findByNodeId(UUID nodeId) {
        var result = cache.findByNodeId(nodeId);
        if (result.isEmpty()) {
            result = nodeRepository.findByNodeId(nodeId);
            cache.save(result.get());
        }
        return result;
    }

    public boolean update(Node node) {
        var result = nodeRepository.update(node);
        if (result) {
            return cache.update(node);
        }
        return false;
    }

    public boolean delete(UUID nodeId) {
        var result = nodeRepository.delete(nodeId);
        if (result) {
            return cache.delete(nodeId);
        }
        return false;
    }

    public Node save(Node node) {
        nodeRepository.save(node);
        cache.save(node);
        return node;
    }
}
