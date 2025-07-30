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
/*        calendarService.getAllCalendars();
        List<CalendarDTO> events = new ArrayList<>();
        events.add(new CalendarDTO("Meeting with team", "2025-07-20T10:00:00"));
        events.add(new CalendarDTO("Project deadline", "2025-07-22T17:00:00"));*/
        return ResponseEntity.ok(calendarService.getAllCalendars());
    }
}
