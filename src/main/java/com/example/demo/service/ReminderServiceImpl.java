package com.example.demo.service;

import com.example.demo.dto.ReminderDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Profile;
import com.example.demo.model.ProfileReminder;
import com.example.demo.model.Reminder;
import com.example.demo.model.User;
import com.example.demo.repository.ProfileReminderRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.ReminderRepository;
import com.example.demo.service.impl.ReminderService;
import com.example.demo.util.DateTimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final UserService userService;
    private final ProfileReminderRepository profileReminderRepository;
    private final ProfileRepository profileRepository;
    private final ReminderRepository reminderRepository;

    public ResponseDTO createReminder(ReminderDTO reminderDTO) {
        Optional<Reminder> existingReminder = reminderRepository.findByTitle(reminderDTO.getTitle());
        if (existingReminder.isPresent()) {
            return new ResponseDTO("Reminder with this title already exists", "error", null);
        }

        Reminder reminder = buildReminder(reminderDTO);
        reminder = reminderRepository.save(reminder);

        saveProfileRemainder(reminder.getId());
        return new ResponseDTO("Reminder created successfully", "success", reminderDTO);
    }

    private Reminder buildReminder(ReminderDTO reminderDTO) {
        return new Reminder.Builder()
                .title(reminderDTO.getTitle())
                .date(Optional.ofNullable(DateTimeConverter.convertDateStringToDate(reminderDTO.getDate()))
                        .orElseGet(Date::new))
                .time(Optional.ofNullable(DateTimeConverter.convertTimeStringToTime(reminderDTO.getTime()))
                        .orElseGet(() -> new java.sql.Time(System.currentTimeMillis())))
                .notified(Boolean.parseBoolean(reminderDTO.getNotified()))
                .build();
    }

    private Boolean saveProfileRemainder(Long reminderId) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Profile profile = profileRepository.findProfilesByUserId(user.getUserId())
                .stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> profileRepository.saveAndFlush(new Profile()));

        ProfileReminder profileReminder = ProfileReminder.builder()
                .profileId(profile.getId())
                .reminderId(reminderId)
                .build();

        profileReminderRepository.saveAndFlush(profileReminder);
        return true; // Assuming the save operation is successful
    }
}
