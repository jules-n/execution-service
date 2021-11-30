package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.User;
import com.ynero.ss.execution.domain.dto.UserDTO;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    boolean save(UserDTO dto);
    boolean addRights(String username, Set<String> rights);
    boolean update(UserDTO dto);
    boolean delete(String username);
    Optional<User> findByUsername(String username);
}
