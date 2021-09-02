package com.ynero.ss.excecution.services.bl;

import com.ynero.ss.excecution.domain.dto.PipelineDTO;
import com.ynero.ss.excecution.domain.dto.PipelineGetDTO;
import dtos.PipelineDevicesDTO;

public interface PipelineService {
    PipelineGetDTO find(String pipelineId);
    boolean update(PipelineDTO dto, String pipelineId);
    boolean delete(String pipelineId);
    String create(PipelineDTO dto);
    boolean addPipelineToPort(PipelineDevicesDTO dto);
}
