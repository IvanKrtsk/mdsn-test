package org.ikrotsyuk.mdsn.booktrackerservice.service.implementation;

import jakarta.transaction.Transactional;
import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.dto.OperationDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.*;
import org.ikrotsyuk.mdsn.booktrackerservice.mappers.AvailableBooksMapper;
import org.ikrotsyuk.mdsn.booktrackerservice.repository.AvailableBooksRepository;
import org.ikrotsyuk.mdsn.booktrackerservice.service.ITrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrackerService implements ITrackerService {
    private final AvailableBooksRepository availableBooksRepository;
    private final AvailableBooksMapper availableBooksMapper;

    @Autowired
    public TrackerService(AvailableBooksRepository availableBooksRepository, AvailableBooksMapper availableBooksMapper) {
        this.availableBooksRepository = availableBooksRepository;
        this.availableBooksMapper = availableBooksMapper;
    }

    public List<AvailableBookDTO> getAvailableBooks(){
        List<AvailableBookEntity> availableBookList = availableBooksRepository.findAllByIsAvailableTrue();
        if(availableBookList.isEmpty())
            throw new BooksNotFoundException();
        else
            return availableBooksMapper.toDTOList(availableBookList);
    }

    @Transactional
    public AvailableBookDTO takeBook(int id, String returnByStr){
        Optional<AvailableBookEntity> availableBookEntity = availableBooksRepository.findById(id);
        if(availableBookEntity.isPresent()){
            LocalDateTime returnBy = LocalDateTime.parse(returnByStr);
            LocalDateTime borrowedAt = LocalDateTime.now();
            if(returnBy.isBefore(borrowedAt))
                throw new ReturnTimeIsBeforeBorrowException(returnBy, borrowedAt);
            AvailableBookEntity bookEntity = availableBookEntity.get();
            if(bookEntity.isAvailable()){
                bookEntity.setAvailable(false);
                bookEntity.setBorrowedAt(LocalDateTime.now());
                bookEntity.setReturnBy(returnBy);
                availableBooksRepository.save(bookEntity);
                return availableBooksMapper.toDTO(bookEntity);
            }else
                throw new BookIsNotAvailableException(id);
        }else
            throw new BookNotFoundByIdException(id);
    }

    @Transactional
    public AvailableBookDTO returnBook(int id){
        Optional<AvailableBookEntity> availableBookEntity = availableBooksRepository.findById(id);
        if(availableBookEntity.isPresent()){
            AvailableBookEntity bookEntity = availableBookEntity.get();
            if(bookEntity.isAvailable())
                throw new BookIsAlreadyAvailableException(id);
            else{
                bookEntity.setAvailable(true);
                bookEntity.setBorrowedAt(null);
                bookEntity.setReturnBy(null);
                availableBooksRepository.save(bookEntity);
                return availableBooksMapper.toDTO(bookEntity);
            }
        }else
            throw new BookNotFoundByIdException(id);
    }

    @Transactional
    public void routeMessage(OperationDTO operationDTO){
        int id = operationDTO.getId();
        String operation = operationDTO.getOperation();
        if(operation.equals("add")) {
            AvailableBookEntity availableBookEntity = new AvailableBookEntity(id, true, null, null);
            availableBooksRepository.save(availableBookEntity);
        } else if (operation.equals("remove")){
            if(availableBooksRepository.existsById(id)){
                availableBooksRepository.deleteById(id);
            }
        }
    }
}
