package com.example.demo.service.impl;

import com.example.demo.dto.ReminderDTO;
import com.example.demo.dto.ResponseDTO;

import java.util.List;

public interface ReminderService {
    List<ReminderDTO> getReminders();
    ResponseDTO createReminder(ReminderDTO reminderDTO);
    ResponseDTO deleteReminder(String id);
}
