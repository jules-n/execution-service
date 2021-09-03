package com.ynero.ss.execution.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDTO {
    private String nodeIdi;
    private String nodeIdj;
    private String outputPortNameOfNodeI;
    private String inputPortNameOfNodeJ;
}
