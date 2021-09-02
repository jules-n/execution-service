package com.ynero.ss.excecution.services.receivers;

import com.ynero.ss.excecution.services.receivers.deviceservice.PipelineReceiver;
import com.ynero.ss.excecution.services.receivers.external.gRPC.PipelineCRUDReceiver;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
public class PipelineServerReceiver {

    @Setter(onMethod_ = {@Value("${grpc.server.port}")})
    private int grpcServerPort;

    private Server server;

    @SneakyThrows
    @PostConstruct
    public void startServer(){
        server = ServerBuilder.forPort(grpcServerPort)
                .addService(new PipelineReceiver())
                .addService(new PipelineCRUDReceiver())
                .build();
        server.start();
    }

    @SneakyThrows
    @PreDestroy
    public void awaitTermination(){
        server.awaitTermination(10, TimeUnit.SECONDS);
    }
}
