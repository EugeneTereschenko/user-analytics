package com.example.assistant.service;

import com.example.assistant.service.impl.Intent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class IntentRegistry {

    private final List<Intent> intents;

    public IntentRegistry(List<Intent> intents) {
        this.intents = intents;
        log.info("Registered {} intents", intents.size());
    }

    public Optional<Intent> matchIntent(String input) {
        return intents.stream()
                .filter(intent -> intent.matches(input))
                .findFirst();
    }

    public List<Intent> getAllIntents() {
        return List.copyOf(intents);
    }
}
