package com.example.assistant.service.impl;

public interface Intent {
    boolean matches(String input);
    String handle(String input);
    String getDescription();
}
