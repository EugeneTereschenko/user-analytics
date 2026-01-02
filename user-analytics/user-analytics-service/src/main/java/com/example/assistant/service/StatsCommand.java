package com.example.assistant.service;


import com.example.assistant.service.impl.Command;
import com.example.demo.service.impl.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatsCommand implements Command {

    private final AnalyticsService analyticsService;

    @Override
    public String getCommandName() {
        return "/stats";
    }

    @Override
    public String getDescription() {
        return "Shows weekly statistics (usage: /stats weekly)";
    }

    @Override
    public String execute(String[] args) {
        if (args.length > 0 && args[0].equals("weekly")) {
            return analyticsService.getWeeklyStats();
        }
        return "Usage: /stats weekly";
    }
}
