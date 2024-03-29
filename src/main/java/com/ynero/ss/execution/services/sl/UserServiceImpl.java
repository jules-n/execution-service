package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.User;
import com.ynero.ss.execution.domain.dto.UserDTO;
import com.ynero.ss.execution.persistence.user.UserRepository;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean save(UserDTO dto) {
        Set<String> roles = new HashSet<>();
        roles.add("data.nodes.read");
        roles.add("data.nodes.update");
        roles.add("data.nodes.create");
        roles.add("data.nodes.delete");
        roles.add("data.pipelines.read");
        roles.add("data.pipelines.update");
        roles.add("data.pipelines.create");
        roles.add("data.pipelines.delete");
        var user = User.builder()
                .userId(UUID.randomUUID())
                .tenantId(dto.getTenantId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .roles(roles)
                .build();
        var result = repository.save(user);
        return result != null;
    }

    @Override
    @SneakyThrows
    public boolean addRights(String username, Set<String> rights) {
        if (username.isEmpty() || rights.isEmpty()) throw new Exception("U cannot add no rights to unexisting user");
        return repository.addRights(username, rights);
    }

    @Override
    @SneakyThrows
    public boolean update(UserDTO dto) {
        var user = repository.findByUsername(dto.getUsername());
        if (user == null) throw new Exception("U cannot change your username");
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return repository.update(user);
    }

    @Override
    public boolean delete(String username) {
        return repository.delete(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.of(repository.findByUsername(username));
    }
}
