package com.ynero.ss.excecution.services.receivers.external.rest;

import com.ynero.ss.excecution.domain.Pipeline;
import com.ynero.ss.excecution.domain.dto.PipelineDTO;
import com.ynero.ss.excecution.persistence.PipelineRepository;
import com.ynero.ss.excecution.services.bl.PipelineService;
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
    private PipelineService pipelineService;

    @PostMapping("/add/to/port")
    private ResponseEntity addPipelineToPort(@RequestBody PipelineDevicesDTO dto) {
        var result = pipelineService.addPipelineToPort(dto);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping
    private ResponseEntity<String> create(@RequestBody PipelineDTO dto) {
        var result = pipelineService.create(dto);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity delete(@RequestParam String pipelineId) {
        var result = pipelineService.delete(pipelineId);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping
    private ResponseEntity<Pipeline> find(@RequestParam String pipelineId) {
        var pipeline = pipelineService.find(pipelineId);
        return pipeline.isEmpty()?ResponseEntity.badRequest().build():ResponseEntity.ok(pipeline.get());
    }

    @PutMapping
    private ResponseEntity update(@RequestBody PipelineDTO dto, @RequestParam String pipelineId) {
        var result = pipelineService.update(dto, pipelineId);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}
