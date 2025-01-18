package org.ikrotsyuk.mdsn.bookstorageservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationDTO {
    @Schema(description = "book id")
    private int id;
    @Schema(description = "book operation type")
    private String operation;
}
