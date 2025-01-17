package org.ikrotsyuk.mdsn.bookstorageservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SimpleBookDTO {
    @NotEmpty(message = "ISBN must not be empty")
    @Pattern(message = "Incorrect format (use ISBN-13)",
            regexp = "^978-[0-9]{1}-[0-9]{3}-[0-9]{5}-[0-9]$")
    private String isbn;
    @NotEmpty(message = "The name must not be empty")
    private String name;
    @NotEmpty(message = "The genre should not be empty")
    private String genre;
    @NotEmpty(message = "Description must not be empty")
    private String description;
    @NotEmpty(message = "Author must not be empty")
    private String author;
}
