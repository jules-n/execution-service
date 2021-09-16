package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.Node;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class NodeCache{

    @Setter(onMethod_ = {@Autowired})
    private RedisTemplate<String, Object> redisTemplate;

    @Setter(onMethod_ = {@Value("${spring.data.redis.expiration}")})
    private int expirationTime;

    private String nodeIdWithPrefix(UUID nodeId) {
        String nodePrefix = "node: ";
        return nodePrefix + nodeId;
    }

    public Optional<Node> findByNodeId(UUID nodeId) {
        var result = (Node)redisTemplate.opsForValue().get(nodeIdWithPrefix(nodeId));
        return Optional.ofNullable(result);
    }

    public boolean update(Node node) {
        var result = delete(node.getNodeId());
        if (!result) {
            return false;
        }
        save(node);
        return true;
    }

    public boolean delete(UUID nodeId) {
        return redisTemplate.delete(nodeIdWithPrefix(nodeId));
    }

    public Node save(Node node) {
        redisTemplate.opsForValue().set(nodeIdWithPrefix(node.getNodeId()), node, expirationTime, TimeUnit.SECONDS);
        return node;
    }
}
