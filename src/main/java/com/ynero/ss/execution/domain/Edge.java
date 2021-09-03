package com.ynero.ss.execution.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Edge {
    private UUID nodeIdi;
    private UUID nodeIdj;
    private String outputPortNameOfNodeI;
    private String inputPortNameOfNodeJ;
}
