package com.joshiai.emailwriter.controller;

import com.joshiai.emailwriter.EmailRequest;
import com.joshiai.emailwriter.service.EmailGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
//@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGenerateController {

    private final EmailGeneratorService emailGeneratorService;

    public EmailGenerateController(EmailGeneratorService emailGeneratorService) {
        this.emailGeneratorService = emailGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
        String response = emailGeneratorService.generateEmail(emailRequest);
        return ResponseEntity.ok(response);
    }

}
