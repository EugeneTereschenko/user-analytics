#!/bin/bash

#
# © 2025 Yevhen Tereshchenko
# All rights reserved.
#
#

curl -X POST http://localhost:8085/doctor-service/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "email": "dr.smith@hospital.com",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1980-05-15",
    "gender": "MALE",
    "licenseNumber": "MD123456",
    "specialization": "Cardiology",
    "qualifications": ["MD", "MBBS", "Fellowship in Cardiology"],
    "languages": ["English", "Spanish"],
    "yearsOfExperience": 15,
    "consultationFee": 150.00,
    "biography": "Experienced cardiologist with 15 years of practice",
    "department": "Cardiology",
    "roomNumber": "301",
    "joinedDate": "2020-01-15"
  }'

