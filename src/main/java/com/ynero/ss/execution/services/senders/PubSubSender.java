package com.ynero.ss.execution.services.senders;

import com.google.api.core.ApiFuture;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class PubSubSender {

    public boolean send(String message, String projectId, String topic) {
        TopicName topicName = TopicName.of(projectId, topic);

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
            publisher.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
