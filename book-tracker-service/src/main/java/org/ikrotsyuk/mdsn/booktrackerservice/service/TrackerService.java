package org.ikrotsyuk.mdsn.booktrackerservice.service;

import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.ikrotsyuk.mdsn.booktrackerservice.mappers.AvailableBooksMapper;
import org.ikrotsyuk.mdsn.booktrackerservice.repository.AvailableBooksRepository;
import org.jvnet.hk2.annotations.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrackerService {
    private final AvailableBooksRepository availableBooksRepository;
    private final AvailableBooksMapper availableBooksMapper;

    @Autowired
    public TrackerService(AvailableBooksRepository availableBooksRepository, AvailableBooksMapper availableBooksMapper) {
        this.availableBooksRepository = availableBooksRepository;
        this.availableBooksMapper = availableBooksMapper;
    }

    public List<AvailableBookDTO> getAvailableBooks(){
        List<AvailableBookEntity> availableBookList = availableBooksRepository.findAllByAvailableTrue();
        if(availableBookList.isEmpty())
            return null;
        else
            return availableBooksMapper.toDTOList(availableBookList);
    }

    public AvailableBookDTO takeBook(int id, LocalDateTime borrowedAt, LocalDateTime returnBy){
//        Optional<AvailableBookEntity> availableBookEntity = availableBooksRepository.findById(id);
        return null;
    }
}
