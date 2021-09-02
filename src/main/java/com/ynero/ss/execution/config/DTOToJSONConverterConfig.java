package com.ynero.ss.execution.config;

import json_converters.DTOToMessageJSONConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOToJSONConverterConfig {
    @Bean
    DTOToMessageJSONConverter getDTOToMessageJSONConverter(){
        return new DTOToMessageJSONConverter();
    }
}
