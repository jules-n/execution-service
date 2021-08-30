package com.ynero.ss.excecution.services.receivers.ds;

import com.ynero.ss.excecution.services.receivers.ds.PipelineReceiver;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PipelineServerReceiver {

    @Setter(onMethod_ = {@Value("${grpc.server.port}")})
    private int grpcServerPort;

    @SneakyThrows
    @PostConstruct
    public void startServer(){
        Server server = ServerBuilder.forPort(grpcServerPort)
                .addService(new PipelineReceiver())
                .build();
        server.start();
        server.awaitTermination();
    }
}
