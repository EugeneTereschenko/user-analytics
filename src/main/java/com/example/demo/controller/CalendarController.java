package com.example.demo.controller;

import com.example.demo.dto.CalendarDTO;
import com.example.demo.service.impl.CalendarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody CalendarDTO calendarDTO) {
        log.info("Creating a new calendar event with details: {}", calendarDTO);
        return ResponseEntity.ok(calendarService.createCalendar(calendarDTO));
    }

    @GetMapping("/events")
    public ResponseEntity<List<CalendarDTO>> getEvents() {
        log.info("Fetching calendar events");
        return ResponseEntity.ok(calendarService.getAllCalendars());
    }
}
