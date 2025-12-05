package com.example.assistant.service;

import com.example.assistant.service.impl.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class CommandRegistry {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandRegistry(List<Command> commandList) {
        commandList.forEach(cmd -> {
            commands.put(cmd.getCommandName(), cmd);
            log.info("Registered command: {}", cmd.getCommandName());
        });
    }

    public Optional<Command> getCommand(String commandName) {
        return Optional.ofNullable(commands.get(commandName));
    }

    public Map<String, Command> getAllCommands() {
        return new HashMap<>(commands);
    }
}
