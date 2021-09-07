package com.ynero.ss.execution.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeBuildDTO {
    private UUID nodeId;
    private String script;
    private List<PortBuildDTO> input;
    private List<PortBuildDTO> output;
}
