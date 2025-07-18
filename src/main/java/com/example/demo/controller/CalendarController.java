package com.example.demo.controller;

import com.example.demo.dto.CalendarDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {



    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody CalendarDTO calendarDTO) {
        log.info("Creating a new calendar event with details: {}", calendarDTO);
        // Logic to create an event would go here
        return ResponseEntity.ok(new HashMap<>().put("message", "Task created successfully"));
    }

    @GetMapping("/events")
    public ResponseEntity<List<CalendarDTO>> getEvents() {
        log.info("Fetching calendar events");
        List<CalendarDTO> events = new ArrayList<>();
        events.add(new CalendarDTO("Meeting with team", "2025-07-20T10:00:00"));
        events.add(new CalendarDTO("Project deadline", "2025-07-22T17:00:00"));
        return ResponseEntity.ok(events);
    }
}
