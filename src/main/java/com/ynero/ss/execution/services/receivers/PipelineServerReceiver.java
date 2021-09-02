package com.ynero.ss.execution.services.receivers;

import com.ynero.ss.execution.services.receivers.deviceservice.PipelineReceiver;
import com.ynero.ss.execution.services.receivers.external.gRPC.NodeCRUDReceiver;
import com.ynero.ss.execution.services.receivers.external.gRPC.PipelineCRUDReceiver;
import com.ynero.ss.execution.services.sl.PipelineService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
public class PipelineServerReceiver {

    @Setter(onMethod_ = {@Autowired})
    private PipelineService pipelineService;

    @Setter(onMethod_ = {@Value("${grpc.server.port}")})
    private int grpcServerPort;

    private Server server;

    @SneakyThrows
    @PostConstruct
    public void startServer(){
        server = ServerBuilder.forPort(grpcServerPort)
                .addService(new PipelineReceiver(pipelineService))
                .addService(new PipelineCRUDReceiver())
                .addService(new NodeCRUDReceiver())
                .build();
        server.start();
    }

    @SneakyThrows
    @PreDestroy
    public void awaitTermination(){
        server.awaitTermination(10, TimeUnit.SECONDS);
    }
}
