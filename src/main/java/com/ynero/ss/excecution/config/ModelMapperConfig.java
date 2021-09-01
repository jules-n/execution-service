package com.ynero.ss.excecution.config;

import com.ynero.ss.excecution.domain.Edge;
import com.ynero.ss.excecution.domain.Pipeline;
import com.ynero.ss.excecution.domain.dto.PipelineDTO;
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
        return modelMapper;
    }

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
}