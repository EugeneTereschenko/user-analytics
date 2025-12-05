package com.example.assistant.service;


import com.example.assistant.service.impl.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final CommandRegistry commandRegistry;

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "Shows all available commands";
    }

    @Override
    public String execute(String[] args) {
        StringBuilder sb = new StringBuilder("Available commands:\n\n");

        commandRegistry.getAllCommands().forEach((name, cmd) ->
                sb.append(name)
                        .append(" - ")
                        .append(cmd.getDescription())
                        .append("\n")
        );

        return sb.toString();
    }
}
