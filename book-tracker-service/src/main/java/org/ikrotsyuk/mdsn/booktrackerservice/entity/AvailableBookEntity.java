package org.ikrotsyuk.mdsn.booktrackerservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "available_books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AvailableBookEntity {
    @Id
    @Schema(description = "book id")
    @Column(name = "id")
    private int id;
    @Schema(description = "is the book available")
    @Column(name = "is_available")
    private boolean isAvailable;
    @Schema(description = "time of taking")
    @Column(name = "borrowed_at")
    private LocalDateTime borrowedAt;
    @Schema(description = "time by which it is necessary to return")
    @Column(name = "return_by")
    private LocalDateTime returnBy;
}
