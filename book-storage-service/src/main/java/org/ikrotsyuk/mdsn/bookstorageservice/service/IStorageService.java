package org.ikrotsyuk.mdsn.bookstorageservice.service;

import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;

import java.util.List;

public interface IStorageService {
    List<BookDTO> getBookList();

    BookDTO getBookById(int id);

    BookDTO getBookByISBN(String isbn);

    BookDTO addBook(SimpleBookDTO simpleBookDTO);

    BookDTO deleteBook(int id);

    public BookDTO updateBookInfo(String isbn, SimpleBookDTO simpleBookDTO);
}
