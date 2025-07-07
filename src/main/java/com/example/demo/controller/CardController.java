package com.example.demo.controller;

import com.example.demo.dto.CardDTO;
import com.example.demo.service.CardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    @PostMapping("/card/send")
    public ResponseEntity<?> sendCard(@RequestBody CardDTO cardDTO) {
        try {
            log.info("Received request to create card: {}", cardDTO);
            return ResponseEntity.ok(cardService.createCard(cardDTO));
        } catch (Exception e) {
            log.error("Error retrieving card {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving card: " + e.getMessage());
        }
    }


}
