package com.rehund.ecommerce.controller;

import com.rehund.ecommerce.common.errors.BadRequestException;
import com.rehund.ecommerce.common.errors.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello world";
    }

    @GetMapping("/bad-request")
    public ResponseEntity<String> badRequestError(){
        throw new BadRequestException("Ada error bad request");
    }

    @GetMapping("/generic-error")
    public ResponseEntity<String> genericError(){
        throw new RuntimeException("Generic Error");
    }

    @GetMapping("/not-found")
    public ResponseEntity<String> notFoundError(){
        throw new ResourceNotFoundException("Data tidak ditemukan");
    }
}
