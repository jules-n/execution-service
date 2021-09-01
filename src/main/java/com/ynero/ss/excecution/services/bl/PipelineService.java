package com.ynero.ss.excecution.services.bl;

import com.ynero.ss.excecution.domain.Pipeline;
import com.ynero.ss.excecution.domain.dto.PipelineDTO;
import dtos.PipelineDevicesDTO;

import java.util.Optional;

public interface PipelineService {
    Optional<Pipeline> find(String pipelineId);
    boolean update(PipelineDTO dto, String pipelineId);
    boolean delete(String pipelineId);
    String create(PipelineDTO dto);
    boolean addPipelineToPort(PipelineDevicesDTO dto);
}
