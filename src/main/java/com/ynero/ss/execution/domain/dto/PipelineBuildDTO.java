package com.ynero.ss.execution.domain.dto;

import java.util.Set;
import java.util.UUID;

public class PipelineBuildDTO {
    private UUID pipelineId;
    private String tenantId;
    private Set<EdgeBuildDTO> edgeBuildDTOS;
}
