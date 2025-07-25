package com.example.demo.controller;

import com.example.demo.dto.ReminderDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.impl.ReminderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;


    @GetMapping
    public ResponseEntity<?> getReminders() {
        log.info("Fetching reminders");
        List<ReminderDTO> reminders = reminderService.getReminders();
        return ResponseEntity.ok(reminders);
    }

    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody ReminderDTO reminderDTO) {
        log.info("Creating reminder: {}", reminderDTO);
        ResponseDTO responseDTO = reminderService.createReminder(reminderDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable String id) {
        log.info("Deleting reminder with ID: {}", id);
        // Here you would typically delete the reminder from a service or database
        ResponseDTO responseDTO = reminderService.deleteReminder(id);
        return ResponseEntity.ok(responseDTO);
    }
}
