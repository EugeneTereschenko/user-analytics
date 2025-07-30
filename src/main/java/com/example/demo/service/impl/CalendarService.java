package com.example.demo.service.impl;

import com.example.demo.dto.CalendarDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Calendar;

import java.util.List;

public interface CalendarService {
    Calendar saveCalendar(CalendarDTO calendarDTO);
    ResponseDTO createCalendar(CalendarDTO calendarDTO);
    List<CalendarDTO> getAllCalendars();
}
