package com.ynero.ss.execution.security;
import com.ynero.ss.execution.domain.User;
import com.ynero.ss.execution.persistence.user.SecurityUserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Setter(onMethod_ = {@Autowired})
    private SecurityUserRepository repository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("No such user");
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role)
        ).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
