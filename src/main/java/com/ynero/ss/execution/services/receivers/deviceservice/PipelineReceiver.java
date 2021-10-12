package com.ynero.ss.execution.services.receivers.deviceservice;

import com.google.protobuf.Empty;
import com.ynero.ss.execution.builder.GraphBuilder;
import com.ynero.ss.execution.services.senders.ResultsToSenderServiceSender;
import com.ynero.ss.pipeline.grpc.PipelineQueryReceiverServiceGrpc;
import json_converters.DTOToMessageJSONConverter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class PipelineReceiver extends PipelineQueryReceiverServiceGrpc.PipelineQueryReceiverServiceImplBase {

   @Setter(onMethod_ = {@Autowired})
    private GraphBuilder graphBuilder;

/*    @Setter(onMethod_ = {@Autowired})
    private ResultsToSenderServiceSender sender;*/


    public void receive(com.ynero.ss.pipeline.dto.proto.PipelinesMessage.PipelineQuery request,
                        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {

        log.info("request in pipelineReceiver: {}", request);
        var results = request.getPipelineDevicesList().stream().map(
                result -> {
                   log.info("result: {}", result);
                   return graphBuilder.build(result, request.getTenantId());
                }
        )
                .collect(Collectors.toList());
        log.info("results: {}", results);
        /*sender.produce(results);*/

        Empty val = Empty.newBuilder().build();
        responseObserver.onNext(val);
        responseObserver.onCompleted();
    }
}
