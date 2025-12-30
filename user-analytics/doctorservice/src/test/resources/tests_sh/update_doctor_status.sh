#!/bin/bash

#
# © 2025 Yevhen Tereshchenko
# All rights reserved.
#
#


curl -X PATCH "http://localhost:8085/doctor-service/api/v1/doctors/1/status?status=ON_LEAVE"