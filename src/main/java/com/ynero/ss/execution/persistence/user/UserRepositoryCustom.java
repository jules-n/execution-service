package com.ynero.ss.execution.persistence.user;

import com.ynero.ss.execution.domain.User;

import java.util.Set;

public interface UserRepositoryCustom {
    boolean addRights(String username, Set<String> rights);
    boolean update(User user);
    boolean delete(String username);
}
