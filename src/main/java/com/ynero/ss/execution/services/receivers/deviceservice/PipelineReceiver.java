package com.ynero.ss.execution.services.receivers.deviceservice;

import com.google.protobuf.Empty;
import com.ynero.ss.execution.builder.GraphBuilder;
import com.ynero.ss.execution.services.senders.DeviceRestSender;
import com.ynero.ss.execution.services.senders.PubSubSender;
import com.ynero.ss.pipeline.grpc.PipelineQueryReceiverServiceGrpc;
import json_converters.DTOToMessageJSONConverter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

@Log4j2
public class PipelineReceiver extends PipelineQueryReceiverServiceGrpc.PipelineQueryReceiverServiceImplBase {

    @Setter(onMethod_ = {@Autowired})
    private GraphBuilder graphBuilder;

    @Setter(onMethod_ = {@Autowired})
    private DeviceRestSender deviceRestSender;

    @Setter(onMethod_ = {@Value("${spring.cloud.stream.bindings.output.destination.result}")})
    private String topicOnResult;

    @Setter(onMethod_ = {@Value("${spring.cloud.gcp.project-id}")})
    private String projectId;

    @Setter(onMethod_ = {@Autowired})
    private DTOToMessageJSONConverter converter;

    @Setter(onMethod_ = {@Autowired})
    private PubSubSender pubSubSender;


    public void receive(com.ynero.ss.pipeline.dto.proto.PipelinesMessage.PipelineQuery request,
                        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
        var results = request.getPipelineDevicesList().stream().map(
                result -> graphBuilder.build(result)
        )
                .collect(Collectors.toList());
        var message = converter.serialize(results);
        pubSubSender.send(message, projectId, topicOnResult);

        log.info("pipeline: {}", request);
        Empty val = Empty.newBuilder().build();
        responseObserver.onNext(val);
        responseObserver.onCompleted();
    }
}
