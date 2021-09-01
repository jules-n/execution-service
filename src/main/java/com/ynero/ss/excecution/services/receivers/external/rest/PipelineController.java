package com.ynero.ss.excecution.services.receivers.external.rest;

import com.ynero.ss.excecution.domain.dto.PipelineDTO;
import com.ynero.ss.excecution.persistence.PipelineRepository;
import com.ynero.ss.excecution.services.senders.ds.PipelinePubSubSender;
import dtos.PipelineDevicesDTO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pipelines")
public class PipelineController {

    @Setter(onMethod_ = {@Autowired})
    private PipelinePubSubSender pubSubSender;

    @Setter(onMethod_ = {@Autowired})
    private PipelineRepository pipelineRepository;

    @PostMapping("/to/port/adding")
    private ResponseEntity addPipelineToPort(@RequestBody PipelineDevicesDTO dto) {
        if (pubSubSender.send(dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    private ResponseEntity<String> create(PipelineDTO dto) {
        return null;
    }
}
