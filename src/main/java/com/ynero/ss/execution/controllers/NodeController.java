package com.ynero.ss.execution.controllers;

import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;
import com.ynero.ss.execution.services.sl.NodeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("nodes")
@Log4j2
public class NodeController {

    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @PostMapping
    @PreAuthorize("@authService.mayCreateNodes(authentication, #dto.tenantId)")
    public ResponseEntity<String> create(@RequestBody NodeDTO dto, Authentication authentication) {
        if (dto.getUsername() == null) {
            var username = ((UserDetails)authentication.getPrincipal()).getUsername();
            dto.setUsername(username);
        }
        var nodeId = nodeService.save(dto);
        return new ResponseEntity<String>(nodeId, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("@authService.mayDeleteNodes(authentication, #nodeId)")
    public ResponseEntity delete(@RequestParam String nodeId, Authentication authentication) {
        var nodeWasDeleted = nodeService.delete(nodeId);
        return nodeWasDeleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    @GetMapping("/{tenantId}")
    @PreAuthorize("@authService.mayGetTenantsNodes(authentication, #tenantId)")
    public ResponseEntity<List<NodeGetDTO>> findAllTenantsNodes(@PathVariable("tenantId") String tenantId) {
        var node = nodeService.getAllTenantsNodes(tenantId);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/my")
    @PreAuthorize("@authService.mayGetNodes(authentication)")
    public ResponseEntity<List<NodeGetDTO>> findAllUsersNodes(Authentication authentication) {
        var username = ((UserDetails)authentication.getPrincipal()).getUsername();
        var node = nodeService.getAllUsersNodes(username);
        return ResponseEntity.ok(node);
    }

    @PutMapping
    @PreAuthorize("@authService.mayUpdateNodes(authentication, #nodeId)")
    public ResponseEntity update(@RequestBody NodeDTO dto, @RequestParam String nodeId) {
        var result = nodeService.update(dto, nodeId);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
