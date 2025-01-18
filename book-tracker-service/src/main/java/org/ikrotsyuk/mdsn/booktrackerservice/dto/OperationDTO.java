package org.ikrotsyuk.mdsn.booktrackerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OperationDTO {
    @Schema(description = "book id")
    private int id;
    @Schema(description = "book operation type")
    private String operation;
}
