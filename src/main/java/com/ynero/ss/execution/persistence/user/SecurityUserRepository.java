package com.ynero.ss.execution.persistence.user;

import com.ynero.ss.execution.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SecurityUserRepository extends MongoRepository<User, UUID> {
    User findByUsername(String username);
}