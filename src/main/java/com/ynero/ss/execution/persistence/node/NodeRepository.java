package com.ynero.ss.execution.persistence.node;

import com.ynero.ss.execution.domain.Node;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NodeRepository extends MongoRepository<Node, UUID>, NodeRepositoryCustom {
    Optional<Node> findByNodeId(UUID nodeId);
}
