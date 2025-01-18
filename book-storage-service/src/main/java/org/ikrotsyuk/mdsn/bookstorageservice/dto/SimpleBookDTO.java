package org.ikrotsyuk.mdsn.bookstorageservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SimpleBookDTO {
    @NotEmpty(message = "ISBN must not be empty")
    @Pattern(message = "Incorrect format (use ISBN-13)",
            regexp = "^978-[0-9]{1}-[0-9]{3}-[0-9]{5}-[0-9]$")
    private String isbn;
    @Schema(description = "book name")
    @NotEmpty(message = "The name must not be empty")
    private String name;
    @Schema(description = "book genre")
    @NotEmpty(message = "The genre should not be empty")
    private String genre;
    @Schema(description = "book description")
    @NotEmpty(message = "Description must not be empty")
    private String description;
    @Schema(description = "book author")
    @NotEmpty(message = "Author must not be empty")
    private String author;
}
