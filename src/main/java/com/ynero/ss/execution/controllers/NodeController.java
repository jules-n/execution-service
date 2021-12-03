package com.ynero.ss.execution.controllers;

import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;
import com.ynero.ss.execution.services.sl.NodeServiceProxy;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("nodes")
@Log4j2
public class NodeController {

    private final NodeServiceProxy nodeService;

    public NodeController(NodeServiceProxy nodeService) {
        this.nodeService = nodeService;
    }

    @PostMapping
    private ResponseEntity<String> create(@RequestBody NodeDTO dto, Authentication authentication) {
        var nodeId = nodeService.save(dto, authentication);
        return new ResponseEntity<String>(nodeId, HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity delete(@RequestParam String nodeId, Authentication authentication) {
        var nodeWasDeleted = nodeService.delete(nodeId, authentication);
        return nodeWasDeleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    @GetMapping("/{tenantId}")
    private ResponseEntity<List<NodeGetDTO>> findAllTenantsNodes(@PathVariable("tenantId") String tenantId) {
        var node = nodeService.getAllTenantsNodes(tenantId);
        return ResponseEntity.ok(node);
    }

    @PutMapping
    private ResponseEntity update(@RequestBody NodeDTO dto, @RequestParam String nodeId) {
        /*var result = nodeService.update(dto, nodeId);*/
        return /*result*/ true ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
