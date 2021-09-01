package com.ynero.ss.analytics_service.config;

import com.ynero.ss.analytics_service.domain.Port;
import com.ynero.ss.analytics_service.domain.PortsDataRecord;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dtos.PortValueDTO;

import java.util.UUID;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(portValueDTOToPortsDataRecordConverter);
        return modelMapper;
    }

    private Converter<PortValueDTO, PortsDataRecord> portValueDTOToPortsDataRecordConverter = new AbstractConverter<>() {
        protected PortsDataRecord convert(PortValueDTO dto) {
            var port = Port.builder()
            .value(dto.getPortValue())
            .name(dto.getPortName());

            var record = PortsDataRecord.builder()
            .deviceId(dto.getDeviceId())
            .tenantId(dto.getTenantId())
            .port(port.build())
            .receivingTime(dto.getReceivingTime())
            .recordId(UUID.randomUUID());

            return record.build();
        }
    };
}