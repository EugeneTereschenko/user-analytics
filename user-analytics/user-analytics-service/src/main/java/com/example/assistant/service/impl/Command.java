package com.example.assistant.service.impl;

public interface Command {
    String getCommandName();
    String getDescription();
    String execute(String[] args);
}
