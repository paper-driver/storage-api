package com.microservice.storageapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/central")
public class CentralController {

    private static final Logger logger = LoggerFactory.getLogger(CentralController.class);

    @GetMapping("/access")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<?> checkAccessibility(){
        logger.info("replying the check request");
        return ResponseEntity.ok("You are allowed to access Storage API.");
    }

}
