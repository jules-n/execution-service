package com.ynero.ss.execution.services.receivers.external.gRPC;

import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.services.sl.PipelineService;
import com.ynero.ss.pipeline.dto.proto.PipelineCRUDServiceGrpc;
import com.ynero.ss.pipeline.dto.proto.PipelineCrud;
import dtos.PipelineDevicesDTO;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class PipelineCRUDReceiver extends PipelineCRUDServiceGrpc.PipelineCRUDServiceImplBase {

    @Setter(onMethod_ = {@Autowired})
    private PipelineService pipelineService;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    @Override
    public void find(com.ynero.ss.pipeline.dto.proto.PipelineCrud.PipelineId request,
                     io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.PipelineGetDTO> responseObserver) {
        var result = pipelineService.find(request.getPipelineId());
        var response = com.ynero.ss.pipeline.dto.proto.PipelineCrud.PipelineGetDTO.newBuilder()
                .setPipelineId(request.getPipelineId())
                .setTenantId(result.getTenantId());
        result.getEdges().forEach(edgeDTO ->
                response.addEdges(
                        PipelineCrud.EdgeDTO.newBuilder()
                                .setNodeIdi(edgeDTO.getNodeIdi())
                                .setNodeIdj(edgeDTO.getNodeIdj())
                                .setOutputPortNameOfNodeI(edgeDTO.getOutputPortNameOfNodeI())
                                .setInputPortNameOfNodeJ(edgeDTO.getInputPortNameOfNodeJ())
                                .build()
                )
        );
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(com.ynero.ss.pipeline.dto.proto.PipelineCrud.PipelineId request,
                       io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.Response> responseObserver) {
        var pipelineWasDeleted = pipelineService.delete(request.getPipelineId());
        var response = PipelineCrud.Response.newBuilder();
        if (pipelineWasDeleted) {
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }


    @Override
    public void addPipelineToPort(com.ynero.ss.pipeline.dto.proto.PipelineCrud.PipelineDevicesDTO request,
                                  io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.Response> responseObserver) {
        PipelineDevicesDTO dto = modelMapper.map(request, PipelineDevicesDTO.class);
        var pipelineWasAdded = pipelineService.addPipelineToPort(dto);
        var response = PipelineCrud.Response.newBuilder();
        if (pipelineWasAdded) {
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void create(com.ynero.ss.pipeline.dto.proto.PipelineCrud.PipelineDTO request,
                       io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.Response> responseObserver) {
        var dto = modelMapper.map(request, PipelineDTO.class);
        var pipelineId = pipelineService.create(dto);
        var response = PipelineCrud.Response.newBuilder()
                .setStatus(200)
                .setResult(pipelineId);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }


    @Override
    public void update(com.ynero.ss.pipeline.dto.proto.PipelineCrud.PipelineUpdateDTO request,
                       io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.Response> responseObserver) {

        var dto = modelMapper.map(request.getPipelineDto(), PipelineDTO.class);
        var result = pipelineService.update(dto,request.getPipelineId());
        var response = PipelineCrud.Response.newBuilder();
        if (result) {
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
