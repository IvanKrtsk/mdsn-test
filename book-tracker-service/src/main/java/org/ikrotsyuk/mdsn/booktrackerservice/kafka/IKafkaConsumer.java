package org.ikrotsyuk.mdsn.booktrackerservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IKafkaConsumer {
    void listenToOperation(String jsonMessage) throws JsonProcessingException;
}
