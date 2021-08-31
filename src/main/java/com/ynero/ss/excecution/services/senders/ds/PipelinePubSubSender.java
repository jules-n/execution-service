package com.ynero.ss.excecution.services.senders.ds;

import com.google.api.core.ApiFuture;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import dtos.PipelineDevicesDTO;
import json_converters.DTOToMessageJSONConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class PipelinePubSubSender {

    @Setter(onMethod_ = {@Value("${spring.cloud.stream.bindings.output.destination}")})
    private String topic;

    @Setter(onMethod_ = {@Value("${spring.cloud.gcp.project-id}")})
    private String projectId;

    @Setter(onMethod_ = {@Autowired})
    private DTOToMessageJSONConverter converter;

    public boolean send(PipelineDevicesDTO dto) {
        TopicName topicName = TopicName.of(projectId, topic);
        var message = converter.serialize(dto);

        try (Publisher publisher = new Publisher(topicName)){
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage
                    .newBuilder().setData(data)
                    .build();
            ApiFuture<String> messageIdFuture = publisher.getPublisher().publish(pubsubMessage);
            var messageId = messageIdFuture.get();
            log.info("messageId: {}", messageId);
            return messageIdFuture.isDone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Getter
    class Publisher implements AutoCloseable {

        private com.google.cloud.pubsub.v1.Publisher publisher;

        public Publisher(TopicName topicName) throws IOException {
            this.publisher = com.google.cloud.pubsub.v1.Publisher.newBuilder(topicName).build();
        }

        @Override
        public void close() throws Exception {
            publisher.shutdown();
            publisher.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
}
