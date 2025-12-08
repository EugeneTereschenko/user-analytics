#!/bin/bash

# Test 1: Health check
curl http://localhost:8080/api/assistant/test

# Expected: "Assistant controller is working!"

# Test 2: Help command
curl -X POST http://localhost:8080/api/assistant/message \
  -H "Content-Type: application/json" \
  -d '{"text": "/help"}'

# Expected: List of all commands

# Test 3: Users command
curl -X POST http://localhost:8080/api/assistant/message \
  -H "Content-Type: application/json" \
  -d '{"text": "/users today"}'

# Expected: "Today we had X new users."

# Test 4: Natural language
curl -X POST http://localhost:8080/api/assistant/message \
  -H "Content-Type: application/json" \
  -d '{"text": "hello"}'

# Expected: Greeting response