package org.ikrotsyuk.mdsn.bookstorageservice.mappers;

import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "isDeleted", ignore = true)
    BookEntity toEntity(BookDTO bookDTO);

    BookDTO toDTO(BookEntity bookEntity);

    List<BookDTO> toDTO(List<BookEntity> bookEntityList);

    @Mapping(target = "id", ignore = true)
    BookDTO toDTO(SimpleBookDTO bookDTO);
}
