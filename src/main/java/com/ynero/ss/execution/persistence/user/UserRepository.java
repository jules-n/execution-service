package com.ynero.ss.execution.persistence.user;

import com.ynero.ss.execution.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID>, UserRepositoryCustom {
    User findByUsername(String username);
}
