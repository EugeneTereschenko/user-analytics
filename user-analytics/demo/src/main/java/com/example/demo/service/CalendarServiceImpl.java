package com.example.demo.service;

import com.example.demo.dto.CalendarDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Calendar;
import com.example.demo.model.Profile;
import com.example.demo.model.ProfileCalendar;
import com.example.demo.model.User;
import com.example.demo.repository.CalendarRepository;
import com.example.demo.repository.ProfileCalendarRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.impl.CalendarService;
import com.example.demo.util.DateTimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CalendarServiceImpl implements CalendarService {

    private final ProfileCalendarRepository profileCalendarRepository;
    private final CalendarRepository calendarRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;

    public Calendar saveCalendar(CalendarDTO calendarDTO) {
        Calendar calendar = new Calendar.Builder()
                .title(calendarDTO.getTitle())
                .date(DateTimeConverter.convertDateStringToDate(calendarDTO.getDate()))
                .build();

        return calendarRepository.save(calendar);

    }

    public ResponseDTO createCalendar(CalendarDTO calendarDTO) {
        if (calendarRepository.findByTitle(calendarDTO.getTitle()).isPresent()) {
            return ResponseDTO.builder()
                    .status("error")
                    .message("Calendar with this name already exists")
                    .build();
        }

        Calendar calendar = new Calendar.Builder()
                .title(calendarDTO.getTitle())
                .date(DateTimeConverter.convertDateStringToDate(calendarDTO.getDate()))
                .build();

        calendar = calendarRepository.save(calendar);
        saveProfileCalendar(calendar.getId());

        return ResponseDTO.builder()
                .status("success")
                .message("Calendar created successfully")
                .data(calendar)
                .build();
    }


    public List<CalendarDTO> getAllCalendars() {
        return calendarRepository.findCalendarsByUserId(userService.getAuthenticatedUser().get().getId())
                .stream()
                .map(calendar -> CalendarDTO.builder()
                        .title(calendar.getTitle())
                        .date(DateTimeConverter.convertDateToString(calendar.getDate()))
                        .build())
                .toList();
    }


    private Boolean saveProfileCalendar(Long calendarId) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Profile profile = profileRepository.findProfilesByUserId(user.getId())
                .stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> profileRepository.saveAndFlush(new Profile()));

        ProfileCalendar profileCalendar = ProfileCalendar.builder()
                .profileId(profile.getId())
                .calendarId(calendarId)
                .build();

        profileCalendarRepository.saveAndFlush(profileCalendar);
        return true; // Assuming the save operation is successful
    }
}
