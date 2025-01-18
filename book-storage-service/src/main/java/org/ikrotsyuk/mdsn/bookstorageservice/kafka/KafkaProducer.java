package org.ikrotsyuk.mdsn.bookstorageservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.OperationDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.SendAddOperationException;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.SendRemoveOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOperationMessage(OperationDTO operationDTO){
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(operationDTO);
        } catch (JsonProcessingException e) {
            int id = operationDTO.getId();
            if(operationDTO.getOperation().equals("add"))
                throw new SendAddOperationException(id);
            else
                throw new SendRemoveOperationException(id);
        }
        kafkaTemplate.send("sendOperationToTrackerTopic", jsonMessage);
    }

}
