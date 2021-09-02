package com.ynero.ss.execution.persistence.pipeline;

import com.ynero.ss.execution.domain.Pipeline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PipelineRepository extends MongoRepository<Pipeline, UUID>, PipelineRepositoryCustom {
    Optional<Pipeline> findByPipelineId(UUID pipelineId);
}
