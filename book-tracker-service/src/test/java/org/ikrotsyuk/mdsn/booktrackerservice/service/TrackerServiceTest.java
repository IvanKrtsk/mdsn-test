package org.ikrotsyuk.mdsn.booktrackerservice.service;

import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.*;
import org.ikrotsyuk.mdsn.booktrackerservice.mappers.AvailableBooksMapper;
import org.ikrotsyuk.mdsn.booktrackerservice.repository.AvailableBooksRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackerServiceTest {
    @InjectMocks
    private TrackerService trackerService;
    @Mock
    private AvailableBooksRepository availableBooksRepository;
    @Spy
    private AvailableBooksMapper availableBooksMapper = Mappers.getMapper(AvailableBooksMapper.class);

    @Test
    void getAvailableBooks_throwsBooksNotFoundException(){
        when(availableBooksRepository.findAllByIsAvailableTrue()).thenReturn(new ArrayList<>());
        assertThrows(BooksNotFoundException.class, () -> trackerService.getAvailableBooks());
    }

    @Test
    void getAvailableBooks_returnsAvailableBookDTOList(){
        AvailableBookEntity availableBookEntity1 = new AvailableBookEntity(1, true, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        AvailableBookEntity availableBookEntity2 = new AvailableBookEntity(2, true, LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        when(availableBooksRepository.findAllByIsAvailableTrue()).thenReturn(List.of(availableBookEntity1, availableBookEntity2));
        Assertions.assertEquals(2, trackerService.getAvailableBooks().size());
    }

    @Test
    void takeBook_throwsBookNotFoundByIdException(){
        when(availableBooksRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundByIdException.class, () -> trackerService.takeBook(999, LocalDateTime.now().plusDays(1).toString()));
    }

    @Test
    void takeBook_throwsReturnTimeIsBeforeBorrowException(){
        when(availableBooksRepository.findById(anyInt())).thenReturn(Optional.of(new AvailableBookEntity(1, true, null, null)));
        assertThrows(ReturnTimeIsBeforeBorrowException.class, () -> trackerService.takeBook(1, LocalDateTime.now().minusDays(1).toString()));
    }

    @Test
    void takeBook_throwsBookIsNotAvailableException(){
        when(availableBooksRepository.findById(anyInt())).thenReturn(Optional.of(new AvailableBookEntity(1, false, null, null)));
        assertThrows(BookIsNotAvailableException.class, () -> trackerService.takeBook(1, LocalDateTime.now().plusDays(1).toString()));
    }

    @Test
    void takeBook_returnsAvailableBookDTO(){
        when(availableBooksRepository.findById(anyInt())).thenReturn(Optional.of(new AvailableBookEntity(1, true, null, null)));
        AvailableBookDTO methodResult = trackerService.takeBook(1, LocalDateTime.now().plusDays(1).toString());
        assertNotNull(methodResult);
    }

    @Test
    void returnBook_throwsBookNotFoundByIdException(){
        when(availableBooksRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundByIdException.class, () -> trackerService.returnBook(999));
    }

    @Test
    void returnBook_throwsBookIsAlreadyAvailableException(){
        when(availableBooksRepository.findById(anyInt())).thenReturn(Optional.of(new AvailableBookEntity(1, true, null, null)));
        assertThrows(BookIsAlreadyAvailableException.class, () -> trackerService.returnBook(1));
    }

    @Test
    void returnBook_returnsAvailableBookDTO(){
        when(availableBooksRepository.findById(anyInt())).thenReturn(Optional.of(new AvailableBookEntity(1, false, null, null)));
        AvailableBookDTO methodResult = trackerService.returnBook(1);
        assertNotNull(methodResult);
    }
}
