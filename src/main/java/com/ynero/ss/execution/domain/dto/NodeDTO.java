package com.ynero.ss.execution.domain.dto;

import com.mongodb.lang.Nullable;
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
    private String nodeId;
    private String script;
    private String description;
    @Nullable
    private String username;
    private String tenantId;
    private List<String> inputPortsName;
    private List<String> outputPortsName;
}
