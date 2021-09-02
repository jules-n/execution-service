package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.domain.Pipeline;
import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.domain.dto.PipelineGetDTO;
import com.ynero.ss.execution.persistence.node.NodeRepository;
import com.ynero.ss.execution.persistence.pipeline.PipelineRepository;
import com.ynero.ss.execution.services.senders.PubSubSender;
import com.ynero.ss.pipeline.dto.proto.PipelinesMessage;
import dtos.PipelineDevicesDTO;
import dtos.PipelineIdDTO;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import json_converters.DTOToMessageJSONConverter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PipelineServiceImpl implements PipelineService {

    @Setter(onMethod_ = {@Autowired})
    private PubSubSender pubSubSender;

    @Setter(onMethod_ = {@Autowired})
    private PipelineRepository pipelineRepository;

    @Setter(onMethod_ = {@Autowired})
    private NodeRepository nodeRepository;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    @Setter(onMethod_ = {@Autowired})
    private GroovyShell groovyShell;

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
        if (!pipeline.isEmpty()) {
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
        pipeline = pipelineRepository.save(pipeline);
        return pipeline.getPipelineId().toString();
    }

    @Override
    public boolean addPipelineToPort(PipelineDevicesDTO dto) {
        var message = converter.serialize(dto);
        var result = pubSubSender.send(message, projectId, topicOnAdd);
        return result;
    }

    @Override
    public void execute(PipelinesMessage.PipelineQuery executePipelineQuery) {
        // TODO: add tree traversal here and find correct nodes execution order
        var nodes = new ArrayList<Node>();

        for (var node : graph.nodes) {
            var scriptStr = node.getScript();
            var binding = new Binding();


            // here we apply input params naming convention
            var params = new HashMap<String, Object>();
            node.getInputPorts().each(port -> {
                params.put(port.getName(), port.getValue());
            });
            binding.setVariable("params", params);
            var script = groovyShell.parse(scriptStr);
            script.setBinding(binding);


            // apply output params naming conventions
            var scriptResult = (Map<String, Object>) script.run();
            node.getOutputPorts().each(outPort -> {
                // !!! For device nodes we need to get values NOT from script, but from "executePipelineQuery" object
                // !!! for output device nodes, we need to send pubsub/kafka event "node.deviceId on port outPort.getName() set value outPort.getValue()"
                // use polymorphism here
                var outPortValue = scriptResult.get(outPort.getName());
                outPort.setValue(outPortValue);
            });

            // walk over all connected downstream nodes, and set values on their input ports
            node.getOutputPorts().each(outPort -> {
                graph.forEachAllConnectedOf(node, outPort, (connectedNode, connectedPort) -> {
                    connectedPort.setValue(outPort.getValue());
                });
            });
        }
    }
}
