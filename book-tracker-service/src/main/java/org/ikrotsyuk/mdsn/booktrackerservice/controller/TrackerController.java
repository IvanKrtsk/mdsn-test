package org.ikrotsyuk.mdsn.booktrackerservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-tracker")
public class TrackerController {
    @GetMapping("/saybye")
    public String say(){
        return "bye";
    }
}
