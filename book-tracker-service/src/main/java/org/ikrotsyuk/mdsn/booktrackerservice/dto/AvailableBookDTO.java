package org.ikrotsyuk.mdsn.booktrackerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableBookDTO {
    @Schema(description = "book id")
    private Integer id;
    @Schema(description = "is the book available")
    private boolean isAvailable;
    @Schema(description = "time of taking")
    private LocalDateTime borrowedAt;
    @Schema(description = "time by which it is necessary to return")
    private LocalDateTime returnBy;
}
