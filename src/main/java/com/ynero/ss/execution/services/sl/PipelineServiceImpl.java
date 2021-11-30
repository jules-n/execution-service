package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.Pipeline;
import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.domain.dto.PipelineGetDTO;
import com.ynero.ss.execution.persistence.pipeline.PipelineRepository;
import com.ynero.ss.execution.services.senders.PubSubSender;
import dtos.PipelineDevicesDTO;
import dtos.PipelineIdDTO;
import json_converters.DTOToMessageJSONConverter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PipelineServiceImpl implements PipelineService {

    @Setter(onMethod_ = {@Autowired})
    private PubSubSender pubSubSender;

    @Setter(onMethod_ = {@Autowired})
    private PipelineRepository pipelineRepository;

    @Setter(onMethod_ = {@Autowired})
    private UserService userService;

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

    @Override
    public PipelineGetDTO find(String pipelineId) {
        var id = UUID.fromString(pipelineId);
        var pipeline = pipelineRepository.findByPipelineId(id);
        if(!pipeline.isEmpty()){
          var dto = modelMapper.map(pipeline.get(), PipelineGetDTO.class);
          return dto;
        }
        return null;
    }

    @Override
    public boolean update(PipelineDTO dto, String pipelineId) {
        var pipeline = modelMapper.map(dto, Pipeline.class);
        pipeline.setPipelineId(UUID.fromString(pipelineId));
        var result = pipelineRepository.update(pipeline);
        if (result) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String pipelineId) {
        var id = UUID.fromString(pipelineId);
        var dto = new PipelineIdDTO(id);
        var message = converter.serialize(dto);
        var sendingResult = pubSubSender.send(message, projectId, topicOnDrop);
        if (!sendingResult) {
            return false;
        }
        var pipelineWasDeleted = pipelineRepository.delete(id);
        if (pipelineWasDeleted) {
            return true;
        }
        return false;
    }

    @Override
    public String create(PipelineDTO dto) {
        var pipeline = modelMapper.map(dto, Pipeline.class);
        pipeline.setPipelineId(UUID.randomUUID());
        pipeline.setUserId(userService.findByUsername(dto.getUsername()).get().getUserId());
        pipeline = pipelineRepository.save(pipeline);
        return pipeline.getPipelineId().toString();
    }

    @Override
    public boolean addPipelineToPort(PipelineDevicesDTO dto) {
        var message = converter.serialize(dto);
        var result = pubSubSender.send(message, projectId, topicOnAdd);
        return result;
    }
}
