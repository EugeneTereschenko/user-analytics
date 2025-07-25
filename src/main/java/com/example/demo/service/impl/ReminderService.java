package com.example.demo.service.impl;

import com.example.demo.dto.ReminderDTO;
import com.example.demo.dto.ResponseDTO;

public interface ReminderService {
    ResponseDTO createReminder(ReminderDTO reminderDTO);
}
