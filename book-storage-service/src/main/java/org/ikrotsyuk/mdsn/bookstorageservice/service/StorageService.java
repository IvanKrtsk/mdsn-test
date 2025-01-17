package org.ikrotsyuk.mdsn.bookstorageservice.service;

import jakarta.transaction.Transactional;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.BookNotFoundByISBNException;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.BookNotFoundByIdException;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.BookWithSameISBNFoundException;
import org.ikrotsyuk.mdsn.bookstorageservice.mappers.BookMapper;
import org.ikrotsyuk.mdsn.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Autowired
    public StorageService(BookMapper bookMapper, BookRepository bookRepository){
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> getBookList(){
        List<BookEntity> bookEntityList = bookRepository.findAll();
        if(bookEntityList.isEmpty())
            return new ArrayList<>(0);
        else
            return bookMapper.toDTO(bookEntityList);
    }

    public BookDTO getBookById(int id){
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        return bookEntity.map(bookMapper::toDTO).orElseThrow(() -> new BookNotFoundByIdException(id));
    }

    public BookDTO getBookByISBN(String isbn){
        BookEntity bookEntity = bookRepository.findFirstByIsbn(isbn);
        if(bookEntity != null)
            return bookMapper.toDTO(bookEntity);
        else throw new BookNotFoundByISBNException(isbn);
    }

    @Transactional
    public BookDTO addBook(SimpleBookDTO simpleBookDTO){
        if(bookRepository.existsByIsbn(simpleBookDTO.getIsbn()))
            throw new BookWithSameISBNFoundException(simpleBookDTO.getIsbn());
        BookDTO bookDTO = bookMapper.toDTO(simpleBookDTO);
        bookRepository.save(bookMapper.toEntity(bookDTO));
        return bookDTO;
    }

    @Transactional
    public void deleteBook(int id){
        if(bookRepository.existsById(id))
            bookRepository.deleteById(id);
        else throw new BookNotFoundByIdException(id);
    }

    @Transactional
    public BookDTO updateBookInfo(String isbn, SimpleBookDTO simpleBookDTO){
        BookEntity oldBookEntity = bookRepository.findFirstByIsbn(isbn);
        if(oldBookEntity == null)
            throw new BookNotFoundByISBNException(isbn);
        oldBookEntity.setIsbn(simpleBookDTO.getIsbn());
        oldBookEntity.setName(simpleBookDTO.getName());
        oldBookEntity.setGenre(simpleBookDTO.getGenre());
        oldBookEntity.setDescription(simpleBookDTO.getDescription());
        oldBookEntity.setAuthor(oldBookEntity.getAuthor());
        bookRepository.save(oldBookEntity);
        return bookMapper.toDTO(oldBookEntity);
    }
}
