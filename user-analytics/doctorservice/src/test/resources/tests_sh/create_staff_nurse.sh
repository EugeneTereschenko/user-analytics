#
# © 2025 Yevhen Tereshchenko
# All rights reserved.
#
#


curl -X POST http://localhost:8085/doctor-service/api/v1/staff \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Mary",
    "lastName": "Williams",
    "email": "mary.williams@hospital.com",
    "phoneNumber": "+1234567892",
    "dateOfBirth": "1985-08-20",
    "gender": "FEMALE",
    "employeeId": "EMP001",
    "role": "NURSE",
    "department": "Emergency",
    "shift": "Morning",
    "salary": 65000.00,
    "joinedDate": "2018-03-01"
  }'