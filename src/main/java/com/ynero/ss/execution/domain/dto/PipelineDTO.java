package com.ynero.ss.execution.domain.dto;

import com.ynero.ss.execution.validators.NodeInterconnection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PipelineDTO {
    private String tenantId;
    private String username;
    @NodeInterconnection
    private List<EdgeDTO> edges;
}
