package com.ynero.ss.execution.controllers;

import com.ynero.ss.execution.domain.dto.UserDTO;
import com.ynero.ss.execution.services.sl.UserService;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("users")
public class UserController {

    @Setter(onMethod_ = @Autowired)
    private UserService userService;

    @PostMapping
    private ResponseEntity register(@RequestBody UserDTO dto) {
        var result = userService.save(dto);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping
    private ResponseEntity addRights(@RequestParam String username, @RequestParam Set<String> rights) {
        var result = userService.addRights(username, rights);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping
    private ResponseEntity updateUser(@RequestBody UserDTO dto) {
        var result = userService.update(dto);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping
    private ResponseEntity deleteUser(@RequestParam String username) {
        var result = userService.delete(username);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}
