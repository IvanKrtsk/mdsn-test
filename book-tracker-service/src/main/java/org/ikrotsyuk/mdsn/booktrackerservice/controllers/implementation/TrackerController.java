package org.ikrotsyuk.mdsn.booktrackerservice.controllers.implementation;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.service.implementation.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/book-tracker")
public class TrackerController{
    private final TrackerService trackerService;
    @Autowired
    public TrackerController(TrackerService trackerService){
        this.trackerService = trackerService;
    }

    @PatchMapping("/take/{id}")
    public ResponseEntity<AvailableBookDTO> takeBook(@PathVariable @Parameter(description = "book id") int id, @RequestParam @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$",
            message = "Incorrect data format (use YYYY-MM-DDTHH:MM:SS") String returnBy){
        return ResponseEntity.ok(trackerService.takeBook(id, returnBy));
    }

    @PatchMapping("/return{id}")
    public ResponseEntity<AvailableBookDTO> returnBook(@PathVariable @Parameter(description = "book id") int id){
        return ResponseEntity.ok(trackerService.returnBook(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AvailableBookDTO>> getAvailableBooks(){
        return ResponseEntity.ok(trackerService.getAvailableBooks());
    }
}
