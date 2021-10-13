package com.ynero.ss.execution.services.receivers;

import com.ynero.ss.execution.builder.GraphBuilder;
import com.ynero.ss.execution.services.receivers.deviceservice.PipelineReceiver;
import com.ynero.ss.execution.services.receivers.external.gRPC.NodeCRUDReceiver;
import com.ynero.ss.execution.services.receivers.external.gRPC.PipelineCRUDReceiver;
import com.ynero.ss.execution.services.senders.ResultsToSenderServiceSender;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.management.GarbageCollectorMXBean;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class PipelineServerReceiver {

    @Setter(onMethod_ = {@Value("${grpc.server.port}")})
    private int grpcServerPort;

    @Setter(onMethod_ = {@Autowired})
    private ResultsToSenderServiceSender sender;

    @Setter(onMethod_ = {@Autowired})
    private GraphBuilder graphBuilder;

    private Server server;

    @SneakyThrows
    @PostConstruct
    public void startServer(){
        server = ServerBuilder.forPort(grpcServerPort)
                .addService(new PipelineReceiver(graphBuilder,sender))
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
