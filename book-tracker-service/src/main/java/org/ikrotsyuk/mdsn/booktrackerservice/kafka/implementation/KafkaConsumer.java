package org.ikrotsyuk.mdsn.booktrackerservice.kafka.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ikrotsyuk.mdsn.booktrackerservice.dto.OperationDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.service.implementation.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer implements org.ikrotsyuk.mdsn.booktrackerservice.kafka.KafkaConsumer {
    private final ObjectMapper objectMapper;
    private final TrackerService trackerService;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, TrackerService trackerService) {
        this.objectMapper = objectMapper;
        this.trackerService = trackerService;
    }


    @KafkaListener(topics = "sendOperationToTrackerTopic", groupId = "my_consumer")
    public void listenToOperation(String jsonMessage) throws JsonProcessingException {
        OperationDTO operationDTO = objectMapper.readValue(jsonMessage, OperationDTO.class);
        trackerService.routeMessage(operationDTO);
    }
}
