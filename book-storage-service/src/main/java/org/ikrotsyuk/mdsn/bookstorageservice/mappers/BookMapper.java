package org.ikrotsyuk.mdsn.bookstorageservice.mappers;

import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "isDeleted", ignore = true, defaultValue = "false")
    BookEntity toEntity(BookDTO bookDTO);

    default BookEntity toEntityWithDefault(BookDTO bookDTO) {
        BookEntity bookEntity = toEntity(bookDTO);
        bookEntity.setIsDeleted(false); // Установите начальное значение здесь
        return bookEntity;
    }

    BookDTO toDTO(BookEntity bookEntity);

    List<BookDTO> toDTO(List<BookEntity> bookEntityList);

    @Mapping(target = "id", ignore = true)
    BookDTO toDTO(SimpleBookDTO bookDTO);
}
