package org.ikrotsyuk.mdsn.bookstorageservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorageController {
    @GetMapping("/sayhey")
    public String say(){
        return "hey";
    }
}
