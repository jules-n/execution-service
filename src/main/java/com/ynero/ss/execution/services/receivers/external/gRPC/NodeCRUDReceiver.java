package com.ynero.ss.execution.services.receivers.external.gRPC;

import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.services.sl.NodeService;
import com.ynero.ss.pipeline.dto.proto.NodeCRUDServiceGrpc;
import com.ynero.ss.pipeline.dto.proto.PipelineCrud;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class NodeCRUDReceiver extends NodeCRUDServiceGrpc.NodeCRUDServiceImplBase {

    @Setter(onMethod_ = {@Autowired})
    private NodeService nodeService;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    @Override
    public void find(com.ynero.ss.pipeline.dto.proto.PipelineCrud.NodeId request,
                     io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.NodeGetDTO> responseObserver) {

        //responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(com.ynero.ss.pipeline.dto.proto.PipelineCrud.NodeId request,
                       io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.Response> responseObserver) {
        var nodeWasDeleted = nodeService.delete(request.getNodeId());
        var response = PipelineCrud.Response.newBuilder();
        if (nodeWasDeleted) {
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void create(com.ynero.ss.pipeline.dto.proto.PipelineCrud.NodeDTO request,
                       io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.Response> responseObserver) {
        var dto = modelMapper.map(request, NodeDTO.class);
        var result = nodeService.save(dto);
        var response = PipelineCrud.Response.newBuilder();
        response.setStatus(200);
        response.setResult(result);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void update(PipelineCrud.NodeUpdateDTO request,
                       io.grpc.stub.StreamObserver<com.ynero.ss.pipeline.dto.proto.PipelineCrud.Response> responseObserver) {
        var dto = modelMapper.map(request.getNodeDTO(), NodeDTO.class);
        var result = nodeService.update(dto, request.getNodeId());
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
