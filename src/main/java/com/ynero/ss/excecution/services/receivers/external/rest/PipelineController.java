package com.ynero.ss.excecution.services.receivers.external.rest;

import com.ynero.ss.excecution.domain.DevicesPipelineDTO;
import com.ynero.ss.excecution.services.senders.ds.PipelinePubSubSender;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("pipelines")
public class PipelineController {

    @Setter(onMethod_ = {@Autowired})
    private PipelinePubSubSender pubSubSender;

    @PutMapping
    private ResponseEntity addPipelineToPort(@RequestBody DevicesPipelineDTO dto){
       pubSubSender.send(dto);
       return ResponseEntity.ok().build();
    }
}
