package com.ynero.ss.execution.services.receivers.external.rest;

import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.domain.dto.PipelineGetDTO;
import com.ynero.ss.execution.services.sl.PipelineService;
import dtos.PipelineDevicesDTO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private ResponseEntity<String> create(@RequestBody @Valid PipelineDTO dto) {
        var result = pipelineService.create(dto);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity delete(@RequestParam String pipelineId) {
        var result = pipelineService.delete(pipelineId);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping
    private ResponseEntity<PipelineGetDTO> find(@RequestParam String pipelineId) {
        var pipeline = pipelineService.find(pipelineId);
        return pipeline==null?ResponseEntity.badRequest().build():ResponseEntity.ok(pipeline);
    }

    @PutMapping
    private ResponseEntity update(@RequestBody PipelineDTO dto, @RequestParam String pipelineId) {
        var result = pipelineService.update(dto, pipelineId);
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}
