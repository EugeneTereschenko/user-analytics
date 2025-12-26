package com.example.demo.service;

import com.example.demo.dto.UserSummaryDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;

    @Override
    public int countNewUsersToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return userRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    }

    @Override
    public String getWeeklyStats() {
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        int weeklySignups = userRepository.countByCreatedAtAfter(weekAgo);
        int activeUsers = userRepository.countActiveUsersLastWeek(LocalDateTime.now());

        return String.format(
                "This week: %d signups, %d active users",
                weeklySignups,
                activeUsers
        );
    }

    @Override
    public String getMostActiveUser() {
        return userRepository.findMostActiveUser()
                .map(user -> user.getUsername() + " (" + user.getEmail() + ")")
                .orElse("No active users found");
    }

    @Override
    public int getTotalUsers() {
        return (int) userRepository.count();
    }

    @Override
    public Map<String, Object> getUserGrowthData() {
        Map<String, Object> data = new HashMap<>();
        data.put("total", getTotalUsers());
        data.put("today", countNewUsersToday());
        data.put("thisWeek", userRepository.countByCreatedAtAfter(
                LocalDateTime.now().minusWeeks(1)
        ));
        data.put("thisMonth", userRepository.countByCreatedAtAfter(
                LocalDateTime.now().minusMonths(1)
        ));
        return data;
    }

    @Override
    public List<String> getTopActiveUsers(int limit) {
        return userRepository.findTopActiveUsers(limit);
    }


    public UserSummaryDTO getUserSummary(LocalDate start, LocalDate end) {
        UserSummaryDTO userSummaryDTO = userRepository.getUserSummary(start, end);
        log.info("UserSummaryDTO log: " + userSummaryDTO.toString());
        return userSummaryDTO;
    }

    public int[] getSignups(LocalDate start, LocalDate end) {
        List<Object[]> signups = userRepository.getSignups(start, end);
        log.info("Signups log: " + signups.stream()
                .map(arr -> java.util.Arrays.toString(arr))
                .toList());

        int[] array = new int[(int) (end.toEpochDay() - start.toEpochDay()) + 1];
        for (Object[] record : signups) {
            LocalDate date = (LocalDate) record[0];
            Long count = (Long) record[1];
            int index = (int) (date.toEpochDay() - start.toEpochDay());
            array[index] = count.intValue();
        }
        return array;
    }

    public Map<String, Integer> getDeviceBreakdown() {
        List<Object[]> devices = userRepository.getDeviceBreakdown();
        log.info("Device breakdown log: " + devices.stream()
                .map(arr -> java.util.Arrays.toString(arr))
                .toList());
        return devices.stream()
                .collect(java.util.stream.Collectors.toMap(
                        arr -> arr[0] == null ? "unknown" : (String) arr[0],
                        arr -> ((Long) arr[1]).intValue()
                ));
    }


    public Map<String, Integer> getUserLocations() {
        List<Object[]> locations = userRepository.getUserLocations();
        log.info("User locations log: " + locations.stream()
                .map(arr -> java.util.Arrays.toString(arr))
                .toList());
        return locations.stream()
                .collect(java.util.stream.Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> ((Long) arr[1]).intValue()
                ));
    }
}
