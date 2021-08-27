package com.ynero.ss.excecution.services.receiver;
import com.google.protobuf.Empty;
import com.ynero.ss.pipeline.grpc.PipelineQueryReceiverServiceGrpc;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PipelineReceiver extends PipelineQueryReceiverServiceGrpc.PipelineQueryReceiverServiceImplBase {

    public void receive(com.ynero.ss.pipeline.dto.proto.PipelinesMessage.PipelineQuery request,
                        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
        log.info("pipeline: {}", request);
        Empty val = Empty.newBuilder().build();
        responseObserver.onNext(val);
        responseObserver.onCompleted();
    }
}
