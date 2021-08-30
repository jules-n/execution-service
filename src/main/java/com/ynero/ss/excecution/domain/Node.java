package com.ynero.ss.excecution.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Document(collection = Node.COLLECTION_NAME)
public class Node {
    public static final String COLLECTION_NAME = "nodes";
    @Indexed(unique = true)
    private UUID nodeId;
    private String script;
    private String mainMethodName;
}
