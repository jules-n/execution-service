package com.ynero.ss.excecution.services.receivers.external.rest;

import com.ynero.ss.excecution.domain.Node;
import com.ynero.ss.excecution.domain.Pipeline;
import com.ynero.ss.excecution.domain.dto.NodeDTO;
import com.ynero.ss.excecution.domain.dto.PipelineDTO;
import com.ynero.ss.excecution.persistence.NodeRepository;
import com.ynero.ss.excecution.persistence.PipelineRepository;
import dtos.PipelineIdDTO;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(name = "nodes")
public class NodeController {

    @Setter(onMethod_ = {@Autowired})
    private NodeRepository nodeRepository;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    @PostMapping
    private ResponseEntity<String> create(@RequestBody NodeDTO dto) {
        var node = modelMapper.map(dto, Node.class);
        node.setNodeId(UUID.randomUUID());
        node = nodeRepository.save(node);
        return new ResponseEntity<String>(node.getNodeId().toString(), HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity delete(@RequestParam String nodeId) {
        var nodeWasDeleted = nodeRepository.delete(UUID.fromString(nodeId));
        if (nodeWasDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    private ResponseEntity<Node> find(@RequestParam String nodeId) {
        var node = nodeRepository.findByNodeId(UUID.fromString(nodeId));
        if (!node.isEmpty()) {
            return ResponseEntity.ok(node.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    private ResponseEntity update(@RequestBody NodeDTO dto, @RequestParam String nodeId) {
        var node = modelMapper.map(dto, Node.class);
        node.setNodeId(UUID.fromString(nodeId));
        var result = nodeRepository.update(node);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
