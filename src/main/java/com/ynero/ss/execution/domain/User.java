package com.ynero.ss.execution.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = User.COLLECTION_NAME)
public class User {
    public static final String COLLECTION_NAME = "users";
    private UUID userId;
    private String username;
    private String password;
    private Set<String> roles;
    private String tenantId;
}
