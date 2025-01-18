package org.ikrotsyuk.mdsn.booktrackerservice.controllers;

import org.ikrotsyuk.mdsn.booktrackerservice.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-tracker")
public class TrackerController {
    private final TrackerService trackerService;
    @Autowired
    public TrackerController(TrackerService trackerService){
        this.trackerService = trackerService;
    }

    @PatchMapping("/take/{id}")
    public ResponseEntity<?> takeBook(@PathVariable int id){
        return ResponseEntity.ok("take");
    }

    @PatchMapping("/return{id}")
    public ResponseEntity<?> returnBook(@PathVariable int id){
        return ResponseEntity.ok("return");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAvailableBooks(){
        return ResponseEntity.ok("all books");
    }
}
