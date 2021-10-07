package com.ynero.ss.execution.services.senders;

import dtos.ResultDTO;
import json_converters.DTOToMessageJSONConverter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultsToSenderServiceSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DTOToMessageJSONConverter converter;
    @Setter(onMethod_ = {@Value("${spring.kafka.producer.results-sending-data-topic}")})
    private String topic;

    public ResultsToSenderServiceSender(KafkaTemplate<String, String> kafkaTemplate, DTOToMessageJSONConverter converter) {
        this.kafkaTemplate = kafkaTemplate;
        this.converter = converter;
    }

    public void produce(List<List<ResultDTO>> dto) {
        var message = converter.serialize(dto);
        kafkaTemplate.send(topic, message);
        kafkaTemplate.flush();
        kafkaTemplate.destroy();
    }
}
