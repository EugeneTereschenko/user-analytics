#!/bin/bash
# © 2025 Yevhen Tereshchenko
# All rights reserved.
#
#

curl -X POST http://localhost:8081/patient-service/api/v1/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE",
    "bloodGroup": "O+",
    "allergies": ["Penicillin", "Peanuts", "Dust mites"],
    "medicalNotes": "Patient has severe allergic reactions"
  }'