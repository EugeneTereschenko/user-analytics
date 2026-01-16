/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.event;


import com.example.demo.dto.UserEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, UserEventDTO> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public void publishUserCreated(UserEventDTO userEvent) {
        userEvent.setEventType("USER_CREATED");
        kafkaTemplate.send(TOPIC, userEvent.getUserId().toString(), userEvent);
        log.info("Published USER_CREATED event for user: {}", userEvent.getUsername());
    }

    public void publishUserUpdated(UserEventDTO userEvent) {
        userEvent.setEventType("USER_UPDATED");
        kafkaTemplate.send(TOPIC, userEvent.getUserId().toString(), userEvent);
        log.info("Published USER_UPDATED event for user: {}", userEvent.getUsername());
    }

    public void publishUserLogin(UserEventDTO userEvent) {
        userEvent.setEventType("USER_LOGIN");
        kafkaTemplate.send(TOPIC, userEvent.getUserId().toString(), userEvent);
        log.info("Published USER_LOGIN event for user: {}", userEvent.getUsername());
    }
}

