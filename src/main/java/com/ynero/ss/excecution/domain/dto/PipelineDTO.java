package com.ynero.ss.excecution.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class PipelineDTO {
    private String tenantId;
    private Set<EdgeDTO> edges;
}
