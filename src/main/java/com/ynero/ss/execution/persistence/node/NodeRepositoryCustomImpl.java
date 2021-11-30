package com.ynero.ss.execution.persistence.node;

import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.UUID;

public class NodeRepositoryCustomImpl implements NodeRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean update(Node node) {
        Update update = new Update();
        update.set("script", node.getScript());
        update.set("inputPortsName",node.getInputPortsName());
        update.set("outputPortsName",node.getOutputPortsName());

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

    @Override
    public List<Node> getAllUsersNodes(String username) {
        var criteria = new Criteria("username").is(username);
        var query = new Query().addCriteria(criteria);
        var user = mongoTemplate.find(query, User.class).get(0);
        var userId = user.getUserId();
        criteria = new Criteria("userId").is(userId);
        query = new Query().addCriteria(criteria);
        return mongoTemplate.find(query, Node.class);
    }

    @Override
    public List<Node> getAllTenantsNodes(String tenantId) {
        var criteria = new Criteria("tenantId").is(tenantId);
        var query = new Query().addCriteria(criteria);
        return mongoTemplate.find(query, Node.class);
    }
}
