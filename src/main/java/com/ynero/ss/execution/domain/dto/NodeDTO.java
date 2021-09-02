package com.ynero.ss.execution.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO {
    private String script;
    private List<String> args;
}
