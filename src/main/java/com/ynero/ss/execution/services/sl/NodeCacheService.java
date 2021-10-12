package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.persistence.node.NodeRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import services.CacheService;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Primary
public class NodeCacheService {

    @Setter(onMethod_ = {@Autowired})
    private NodeRepository nodeRepository;

    @Setter(onMethod_ = {@Autowired})
    private CacheService<String, Node> cache;

    private final int expirationTime = 1;

    public Optional<Node> findByNodeId(UUID nodeId) {
        var result = cache.get(nodeId.toString());
        if (result.isEmpty()) {
            result = nodeRepository.findByNodeId(nodeId);
            if (result.isEmpty()) throw new RuntimeException("No such node");
            cache.save(result.get().getNodeId().toString(), result.get(), expirationTime, TimeUnit.SECONDS);
        }
        return result;
    }

    public boolean update(Node node) {
        var result = nodeRepository.update(node);
        if (result) {
            cache.delete(node.getNodeId().toString());
            cache.save(node.getNodeId().toString(), node, expirationTime, TimeUnit.SECONDS);
        }
        return false;
    }

    public boolean delete(UUID nodeId) {
        var result = nodeRepository.delete(nodeId);
        if (result) {
            return cache.delete(nodeId.toString());
        }
        return false;
    }

    public Node save(Node node) {
        nodeRepository.save(node);
        cache.save(node.getNodeId().toString(), node, expirationTime, TimeUnit.SECONDS);
        return node;
    }
}
