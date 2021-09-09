package com.ynero.ss.execution.config;

import com.ynero.ss.execution.domain.Edge;
import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.domain.Pipeline;
import com.ynero.ss.execution.domain.dto.EdgeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;
import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.domain.dto.PipelineGetDTO;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.UUID;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(dtoPipelineConverter);
        modelMapper.addConverter(pipelineToPipelineGetDTOConverter);
        modelMapper.addConverter(nodeToNodeGetDTOConverter);
        modelMapper.addConverter(nodeGetDTOToNodeConverter);
        return modelMapper;
    }

    private Converter<NodeGetDTO, Node> nodeGetDTOToNodeConverter = new AbstractConverter<>() {
        protected Node convert(NodeGetDTO dto) {

            var node = Node.builder()
                    .nodeId(UUID.fromString(dto.getNodeId()))
                    .script(dto.getScript())
                    .description(dto.getDescription())
                    .inputPortsName(dto.getInputPortsName())
                    .outputPortsName(dto.getOutputPortsName())
                    .build();
            return node;
        }
    };

    private Converter<Node, NodeGetDTO> nodeToNodeGetDTOConverter = new AbstractConverter<>() {
        protected NodeGetDTO convert(Node node) {

            var dto = NodeGetDTO.builder()
                    .nodeId(node.getNodeId().toString())
                    .script(node.getScript())
                    .description(node.getDescription())
                    .inputPortsName(node.getInputPortsName())
                    .outputPortsName(node.getOutputPortsName())
                    .build();
            return dto;
        }
    };

    private Converter<PipelineDTO, Pipeline> dtoPipelineConverter = new AbstractConverter<>() {
        protected Pipeline convert(PipelineDTO dto) {
            var edges = new ArrayList<Edge>();

            dto.getEdges().forEach(
                    edge -> {
                        edges.add(new Edge(
                                UUID.fromString(edge.getNodeIdi()),
                                UUID.fromString(edge.getNodeIdj()),
                                edge.getOutputPortNameOfNodeI(),
                                edge.getInputPortNameOfNodeJ()
                        ));
                    }
            );

            var pipeline = Pipeline.builder()
                    .tenantId(dto.getTenantId())
                    .edges(edges)
                    .build();

            return pipeline;
        }
    };

    private Converter<Pipeline, PipelineGetDTO> pipelineToPipelineGetDTOConverter = new AbstractConverter<>() {
        protected PipelineGetDTO convert(Pipeline pipeline) {
            var edges = new ArrayList<EdgeDTO>();

            pipeline.getEdges().forEach(
                    edge -> {
                        edges.add(new EdgeDTO(
                                edge.getNodeIdi().toString(),
                                edge.getNodeIdj().toString(),
                                edge.getOutputPortNameOfNodeI(),
                                edge.getInputPortNameOfNodeJ()
                        ));
                    }
            );

            var dto = PipelineGetDTO.builder()
                    .pipelineId(pipeline.getPipelineId().toString())
                    .tenantId(pipeline.getTenantId())
                    .edges(edges)
                    .build();
            return dto;
        }
    };
}