package com.ynero.ss.execution.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeDTO {
    private String script;
    private List<String> inputPortsName;
    private List<String> outputPortsName;
}
