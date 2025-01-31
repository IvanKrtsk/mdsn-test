package org.ikrotsyuk.mdsn.bookstorageservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "book id")
    @Column(name = "id")
    private Integer id;
    @Schema(description = "book isbn")
    @Column(name = "isbn")
    private String isbn;
    @Schema(description = "book name")
    @Column(name = "name")
    private String name;
    @Schema(description = "book genre")
    @Column(name = "genre")
    private String genre;
    @Schema(description = "book description")
    @Column(name = "description")
    private String description;
    @Schema(description = "book author")
    @Column(name = "author")
    private String author;
    @Schema(description = "is the book deleted")
    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
