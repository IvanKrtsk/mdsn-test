package org.ikrotsyuk.mdsn.bookstorageservice.kafka;

import org.ikrotsyuk.mdsn.bookstorageservice.dto.OperationDTO;

public interface IKafkaProducer {
    void sendOperationMessage(OperationDTO operationDTO);
}
