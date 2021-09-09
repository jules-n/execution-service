package com.ynero.ss.execution.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeGetDTO {
    private String nodeId;
    private String script;
    private String description;
    private List<String> inputPortsName;
    private List<String> outputPortsName;
}
