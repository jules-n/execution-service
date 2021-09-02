package com.ynero.ss.execution.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Edge {
    @Field(name = "pi")
    private UUID nodeIdi;
    @Field(name = "pj")
    private UUID nodeIdj;
}
