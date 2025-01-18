package org.ikrotsyuk.mdsn.booktrackerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailableBookDTO {
    @Schema(description = "book id")
    private int id;
    @Schema(description = "is the book available")
    private boolean isBookAvailable;
    @Schema(description = "time of taking")
    private LocalDateTime borrowedAt;
    @Schema(description = "time by which it is necessary to return")
    private LocalDateTime returnBy;
}
