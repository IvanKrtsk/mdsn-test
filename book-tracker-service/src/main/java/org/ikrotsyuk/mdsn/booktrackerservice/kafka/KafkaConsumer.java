package org.ikrotsyuk.mdsn.booktrackerservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaConsumer {
    void listenToOperation(String jsonMessage) throws JsonProcessingException;
}
