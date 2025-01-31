package org.ikrotsyuk.mdsn.booktrackerservice.service;

import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.dto.OperationDTO;

import java.util.List;

public interface ITrackerService {
    List<AvailableBookDTO> getAvailableBooks();

    AvailableBookDTO takeBook(int id, String returnByStr);

    AvailableBookDTO returnBook(int id);

    void routeMessage(OperationDTO operationDTO);
}
