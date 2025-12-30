#!/bin/bash

#
# © 2025 Yevhen Tereshchenko
# All rights reserved.
#
#

curl -X POST http://localhost:8084/doctor-service/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Sarah",
    "lastName": "Johnson",
    "email": "dr.johnson@hospital.com",
    "phoneNumber": "+1234567891",
    "gender": "FEMALE",
    "specialization": "Pediatrics",
    "consultationFee": 100.00,
    "department": "Pediatrics",
    "schedules": [
      {
        "dayOfWeek": "MONDAY",
        "startTime": "09:00:00",
        "endTime": "17:00:00",
        "slotDurationMinutes": 30
      },
      {
        "dayOfWeek": "WEDNESDAY",
        "startTime": "09:00:00",
        "endTime": "17:00:00",
        "slotDurationMinutes": 30
      }
    ]
  }'

