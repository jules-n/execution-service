package com.ynero.ss.excecution.persistence;

import com.ynero.ss.excecution.domain.Pipeline;

import java.util.UUID;

public interface PipelineRepositoryCustom {
    boolean update(Pipeline pipeline);
    boolean delete(UUID pipelineId);
}
