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

import java.util.TreeSet;
import java.util.UUID;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(dtoPipelineConverter);
        modelMapper.addConverter(pipelineToPipelineGetDTOConverter);
        modelMapper.addConverter(nodeToNodeGetDTOConverter);
        return modelMapper;
    }

    private Converter<Node, NodeGetDTO> nodeToNodeGetDTOConverter = new AbstractConverter<>() {
        protected NodeGetDTO convert(Node node) {

            var dto = NodeGetDTO.builder()
                    .nodeId(node.getNodeId().toString())
                    .script(node.getScript())
                    .args(node.getArgs())
                    .build();
            return dto;
        }
    };

    private Converter<PipelineDTO, Pipeline> dtoPipelineConverter = new AbstractConverter<>() {
        protected Pipeline convert(PipelineDTO dto) {
            var edges = new TreeSet<Edge>();

            dto.getEdges().forEach(
                    edge -> {
                        edges.add(new Edge(
                                UUID.fromString(edge.getNodeIdi()),
                                UUID.fromString(edge.getNodeIdj())
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
            var edges = new TreeSet<EdgeDTO>();

            pipeline.getEdges().forEach(
                    edge -> {
                        edges.add(new EdgeDTO(
                                edge.getNodeIdi().toString(),
                                edge.getNodeIdj().toString()
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