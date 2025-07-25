package com.example.demo.controller;

import com.example.demo.dto.ReminderDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.impl.ReminderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        List<ReminderDTO> reminders = List.of(
                new ReminderDTO.Builder()
                        .id("1")
                        .title("Reminder 1")
                        .date("2023-10-01")
                        .time("10:00")
                        .notified("false")
                        .build(),
                new ReminderDTO.Builder()
                        .id("2")
                        .title("Reminder 2")
                        .date("2023-10-02")
                        .time("12:00")
                        .notified("true")
                        .build()
        );
        // Here you would typically fetch reminders from a service or database
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
        return ResponseEntity.ok(new HashMap<>().put("message", "Reminder deleted successfully"));
    }
}
