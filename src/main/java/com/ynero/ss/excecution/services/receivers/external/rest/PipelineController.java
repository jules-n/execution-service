package com.ynero.ss.excecution.services.receivers.external.rest;

import com.ynero.ss.excecution.domain.Pipeline;
import com.ynero.ss.excecution.domain.dto.PipelineDTO;
import com.ynero.ss.excecution.persistence.PipelineRepository;
import com.ynero.ss.excecution.services.senders.PubSubSender;
import dtos.PipelineDevicesDTO;
import dtos.PipelineIdDTO;
import json_converters.DTOToMessageJSONConverter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("pipelines")
public class PipelineController {

    @Setter(onMethod_ = {@Autowired})
    private PubSubSender pubSubSender;

    @Setter(onMethod_ = {@Autowired})
    private PipelineRepository pipelineRepository;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    @Setter(onMethod_ = {@Value("${spring.cloud.stream.bindings.output.destination.add}")})
    private String topicOnAdd;

    @Setter(onMethod_ = {@Value("${spring.cloud.stream.bindings.output.destination.drop}")})
    private String topicOnDrop;

    @Setter(onMethod_ = {@Value("${spring.cloud.gcp.project-id}")})
    private String projectId;

    @Setter(onMethod_ = {@Autowired})
    private DTOToMessageJSONConverter converter;

    @PostMapping("/add/to/port")
    private ResponseEntity addPipelineToPort(@RequestBody PipelineDevicesDTO dto) {
        var message = converter.serialize(dto);
        if (pubSubSender.send(message, projectId, topicOnAdd)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    private ResponseEntity<String> create(@RequestBody PipelineDTO dto) {
        var pipeline = modelMapper.map(dto, Pipeline.class);
        pipeline.setPipelineId(UUID.randomUUID());
        pipeline = pipelineRepository.save(pipeline);
        return new ResponseEntity<String>(pipeline.getPipelineId().toString(), HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity delete(@RequestParam String pipelineId) {
        var id = UUID.fromString(pipelineId);
        var dto = new PipelineIdDTO(id);
        var message = converter.serialize(dto);
        var pipelineWasDeleted = pipelineRepository.delete(id);
        if (pipelineWasDeleted) {
            if (pubSubSender.send(message, projectId, topicOnDrop)) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    private ResponseEntity<Pipeline> find(@RequestParam String pipelineId) {
        var pipeline = pipelineRepository.findByPipelineId(UUID.fromString(pipelineId));
        if (!pipeline.isEmpty()) {
            return ResponseEntity.ok(pipeline.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    private ResponseEntity update(@RequestBody PipelineDTO dto, @RequestParam String pipelineId) {
        var pipeline = modelMapper.map(dto, Pipeline.class);
        pipeline.setPipelineId(UUID.fromString(pipelineId));
        var result = pipelineRepository.update(pipeline);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
