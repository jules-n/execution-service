package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.domain.dto.PipelineGetDTO;
import com.ynero.ss.pipeline.dto.proto.PipelinesMessage;
import dtos.PipelineDevicesDTO;

public interface PipelineService {
    PipelineGetDTO find(String pipelineId);
    boolean update(PipelineDTO dto, String pipelineId);
    boolean delete(String pipelineId);
    String create(PipelineDTO dto);
    boolean addPipelineToPort(PipelineDevicesDTO dto);

    // TODO: revise this method signature
    void execute(PipelinesMessage.PipelineQuery executePipelineQuery);
}
