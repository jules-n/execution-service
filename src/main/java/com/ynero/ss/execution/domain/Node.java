package com.ynero.ss.execution.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Node.COLLECTION_NAME)
public class Node {
    public static final String COLLECTION_NAME = "nodes";
    private UUID nodeId;
    private String script;
    private byte[] compiledScript;
    private List<String> inputPortsName;
    private List<String> outputPortsName;
}