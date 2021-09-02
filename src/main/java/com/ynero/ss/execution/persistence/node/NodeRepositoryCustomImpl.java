package com.ynero.ss.execution.persistence.node;

import com.ynero.ss.execution.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.UUID;

public class NodeRepositoryCustomImpl implements NodeRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean update(Node node) {
        Update update = new Update();
        update.set("script", node.getScript());
        update.set("args", node.getArgs());

        Criteria criteria = new Criteria("nodeId").is(node.getNodeId());

        var result = mongoTemplate.updateFirst(new Query(criteria), update, Node.COLLECTION_NAME);

        return result.wasAcknowledged();
    }

    @Override
    public boolean delete(UUID nodeId) {
        Criteria criteria = new Criteria("nodeId").is(nodeId);
        var result = mongoTemplate.remove(new Query(criteria), Node.COLLECTION_NAME);
        return result.getDeletedCount()==1;
    }
}
