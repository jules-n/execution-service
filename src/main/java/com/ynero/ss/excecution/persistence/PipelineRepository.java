package com.ynero.ss.excecution.persistence;

import com.ynero.ss.excecution.domain.Pipeline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PipelineRepository extends MongoRepository<Pipeline, UUID> {
}
