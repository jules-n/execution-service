package com.ynero.ss.excecution.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = Pipeline.COLLECTION_NAME)
public class Pipeline {
    public static final String COLLECTION_NAME = "pipelines";
    @Indexed(unique = true)
    private UUID pipelineId;
    private String tenantId;
    private Set<Edge> edges;

}
