package com.ynero.ss.excecution;

import com.ynero.ss.excecution.services.receiver.gRPCReceiver;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExecutionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExecutionServiceApplication.class, args);
	}
}
