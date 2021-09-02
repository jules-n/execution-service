package com.ynero.ss.excecution.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PipelineGetDTO {
    private String pipelineId;
    private String tenantId;
    private Set<EdgeDTO> edges;
}
