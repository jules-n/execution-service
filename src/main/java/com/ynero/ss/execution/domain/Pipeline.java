package com.ynero.ss.execution.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = Pipeline.COLLECTION_NAME)
public class Pipeline {
    public static final String COLLECTION_NAME = "pipelines";
    private UUID pipelineId;
    private String tenantId;
    private UUID userId;
    private List<Edge> edges;

}
