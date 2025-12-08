package com.example.assistant.service;


import com.example.assistant.service.impl.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

    private CommandRegistry commandRegistry;

    @Autowired
    public HelpCommand(@Lazy CommandRegistry commandRegistry) {  // ✅ Lazy injection
        this.commandRegistry = commandRegistry;
    }

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
