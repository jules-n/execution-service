package com.ynero.ss.execution.persistence.pipeline;

import com.ynero.ss.execution.domain.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.UUID;

public class PipelineRepositoryCustomImpl implements PipelineRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean update(Pipeline pipeline) {
        Update update = new Update();
        update.set("edges", pipeline.getEdges());
        update.set("tenantId", pipeline.getTenantId());

        Criteria criteria = new Criteria("pipelineId").is(pipeline.getPipelineId());

        var result = mongoTemplate.updateFirst(new Query(criteria), update, Pipeline.COLLECTION_NAME);

        return result.wasAcknowledged();
    }

    @Override
    public boolean delete(UUID pipelineId) {
        Criteria criteria = new Criteria("pipelineId").is(pipelineId);
        var result = mongoTemplate.remove(new Query(criteria), Pipeline.COLLECTION_NAME);
        return result.getDeletedCount()==1;
    }
}
