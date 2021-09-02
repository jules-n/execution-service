package com.ynero.ss.execution.persistence.pipeline;

import com.ynero.ss.execution.domain.Pipeline;

import java.util.UUID;

public interface PipelineRepositoryCustom {
    boolean update(Pipeline pipeline);
    boolean delete(UUID pipelineId);
}
