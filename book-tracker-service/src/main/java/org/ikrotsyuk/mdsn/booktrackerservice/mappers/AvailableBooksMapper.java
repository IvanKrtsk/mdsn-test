package org.ikrotsyuk.mdsn.booktrackerservice.mappers;

import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AvailableBooksMapper {
    AvailableBookEntity toEntity(AvailableBookDTO freeBookDTO);

    AvailableBookDTO toDTO(AvailableBookEntity freeBookEntity);

    List<AvailableBookDTO> toDTOList(List<AvailableBookEntity> availableBookEntityList);
}
