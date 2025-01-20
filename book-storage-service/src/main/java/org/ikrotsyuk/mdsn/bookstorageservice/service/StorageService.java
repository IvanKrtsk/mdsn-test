package org.ikrotsyuk.mdsn.bookstorageservice.service;

import jakarta.transaction.Transactional;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.OperationDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.*;
import org.ikrotsyuk.mdsn.bookstorageservice.kafka.KafkaProducer;
import org.ikrotsyuk.mdsn.bookstorageservice.mappers.BookMapper;
import org.ikrotsyuk.mdsn.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public StorageService(BookMapper bookMapper, BookRepository bookRepository, KafkaProducer kafkaProducer){
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<BookDTO> getBookList(){
        List<BookEntity> bookEntityList = bookRepository.findAllByIsDeletedFalse();
        if(bookEntityList.isEmpty())
            throw new BooksNotFoundException();
        else
            return bookMapper.toDTO(bookEntityList);
    }

    public BookDTO getBookById(int id){
        Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
        if(optionalBookEntity.isPresent()) {
            BookEntity bookEntity = optionalBookEntity.get();
            if(bookEntity.getIsDeleted())
                throw new BookIsDeletedException(id);
            else
                return bookMapper.toDTO(bookEntity);
        } else
            throw new BookNotFoundByIdException(id);
    }

    public BookDTO getBookByISBN(String isbn){
        BookEntity bookEntity = bookRepository.findFirstByIsbn(isbn);
        if(bookEntity != null) {
            if(bookEntity.getIsDeleted())
                throw new BookIsDeletedException(isbn);
            else
                return bookMapper.toDTO(bookEntity);
        } else
            throw new BookNotFoundByISBNException(isbn);
    }

    @Transactional
    public BookDTO addBook(SimpleBookDTO simpleBookDTO){
        final String ADD_MESSAGE = "add";
        if(bookRepository.existsByIsbn(simpleBookDTO.getIsbn())){
            BookEntity bookEntity = bookRepository.findFirstByIsbn(simpleBookDTO.getIsbn());
            if(bookEntity.getIsDeleted()) {
                bookEntity.setIsDeleted(false);
                bookRepository.save(bookEntity);
                kafkaProducer.sendOperationMessage(new OperationDTO(bookEntity.getId(), ADD_MESSAGE));
                return bookMapper.toDTO(bookEntity);
            }else
                throw new BookWithSameISBNFoundException(simpleBookDTO.getIsbn());
        } else{
            BookDTO bookDTO = bookMapper.toDTO(simpleBookDTO);
            BookEntity bookEntity = bookRepository.save(bookMapper.toEntityWithDefault(bookDTO));
            kafkaProducer.sendOperationMessage(new OperationDTO(bookEntity.getId(), ADD_MESSAGE));
            bookDTO.setId(bookEntity.getId());
            return bookDTO;
        }

    }

    @Transactional
    public BookDTO deleteBook(int id){
        Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
        if(optionalBookEntity.isPresent()) {
            BookEntity bookEntity = optionalBookEntity.get();
            if(bookEntity.getIsDeleted())
                throw new BookIsDeletedException(id);
            else {
                bookEntity.setIsDeleted(true);
                final String REMOVE_MESSAGE = "remove";
                kafkaProducer.sendOperationMessage(new OperationDTO(bookEntity.getId(), REMOVE_MESSAGE));
                return bookMapper.toDTO(bookEntity);
            }
        }
        else throw new BookNotFoundByIdException(id);
    }

    @Transactional
    public BookDTO updateBookInfo(String isbn, SimpleBookDTO simpleBookDTO){
        BookEntity oldBookEntity = bookRepository.findFirstByIsbn(isbn);
        if(oldBookEntity != null){
            if(oldBookEntity.getIsDeleted())
                throw new BookIsDeletedException(isbn);
            else{
                oldBookEntity.setIsbn(simpleBookDTO.getIsbn());
                oldBookEntity.setName(simpleBookDTO.getName());
                oldBookEntity.setGenre(simpleBookDTO.getGenre());
                oldBookEntity.setDescription(simpleBookDTO.getDescription());
                oldBookEntity.setAuthor(oldBookEntity.getAuthor());
                bookRepository.save(oldBookEntity);
                return bookMapper.toDTO(oldBookEntity);
            }
        }else
            throw new BookNotFoundByISBNException(isbn);
    }
}
