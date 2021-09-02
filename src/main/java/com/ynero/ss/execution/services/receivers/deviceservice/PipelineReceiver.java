package com.ynero.ss.execution.services.receivers.deviceservice;
import com.google.protobuf.Empty;
import com.ynero.ss.execution.services.sl.PipelineService;
import com.ynero.ss.pipeline.grpc.PipelineQueryReceiverServiceGrpc;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class PipelineReceiver extends PipelineQueryReceiverServiceGrpc.PipelineQueryReceiverServiceImplBase {
    private PipelineService pipelineService;

    public void receive(com.ynero.ss.pipeline.dto.proto.PipelinesMessage.PipelineQuery request,
                        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
        log.info("pipeline: {}", request);

        // action is async, we respond right away
        Empty val = Empty.newBuilder().build();
        responseObserver.onNext(val);
        responseObserver.onCompleted();

        // TODO: check if we need to start execution asynchronously on another thread
        pipelineService.execute(request);
    }
}
