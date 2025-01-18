package org.ikrotsyuk.mdsn.booktrackerservice.service;

import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.BookIsAlreadyAvailableException;
import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.BookIsNotAvailableException;
import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.BookNotFoundByIdException;
import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.BooksNotFoundException;
import org.ikrotsyuk.mdsn.booktrackerservice.mappers.AvailableBooksMapper;
import org.ikrotsyuk.mdsn.booktrackerservice.repository.AvailableBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        List<AvailableBookEntity> availableBookList = availableBooksRepository.findAllByBookAvailableTrue();
        if(availableBookList.isEmpty())
            throw new BooksNotFoundException();
        else
            return availableBooksMapper.toDTOList(availableBookList);
    }

    public AvailableBookDTO takeBook(int id, LocalDateTime returnBy){
        Optional<AvailableBookEntity> availableBookEntity = availableBooksRepository.findById(id);
        if(availableBookEntity.isPresent()){
            AvailableBookEntity bookEntity = availableBookEntity.get();
            if(bookEntity.isBookAvailable()){
                bookEntity.setBookAvailable(false);
                bookEntity.setBorrowedAt(LocalDateTime.now());
                bookEntity.setReturnBy(returnBy);
                availableBooksRepository.save(bookEntity);
                return availableBooksMapper.toDTO(bookEntity);
            }else
                throw new BookIsNotAvailableException(id);
        }else
            throw new BookNotFoundByIdException(id);
    }

    public AvailableBookDTO returnBook(int id){
        Optional<AvailableBookEntity> availableBookEntity = availableBooksRepository.findById(id);
        if(availableBookEntity.isPresent()){
            AvailableBookEntity bookEntity = availableBookEntity.get();
            if(bookEntity.isBookAvailable())
                throw new BookIsAlreadyAvailableException(id);
            else{
                bookEntity.setBookAvailable(true);
                bookEntity.setBorrowedAt(null);
                bookEntity.setReturnBy(null);
                availableBooksRepository.save(bookEntity);
                return availableBooksMapper.toDTO(bookEntity);
            }
        }else
            throw new BookNotFoundByIdException(id);
    }
}
