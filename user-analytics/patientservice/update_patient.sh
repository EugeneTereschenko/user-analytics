#!/bin/bash

#
# © 2025 Yevhen Tereshchenko
# All rights reserved.
#
#


curl -X PUT http://localhost:8081/patient-service/api/v1/patients/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE",
    "address": {
      "street": "456 Oak Avenue",
      "city": "Los Angeles",
      "state": "CA",
      "zipCode": "90001",
      "country": "USA"
    }
  }'