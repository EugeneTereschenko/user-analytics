package com.healthcare.doctorservice.testutil;

import com.healthcare.doctorservice.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StaffTestBuilder {
    private Long id = 1L;
    private String firstName = "Alice";
    private String lastName = "Smith";
    private String email = "alice.smith@example.com";
    private String phoneNumber = "5551234567";
    private LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
    private Gender gender = Gender.FEMALE;
    private String employeeId = "EMP001";
    private StaffRole role = StaffRole.NURSE;
    private String department = "General";
    private String shift = "Morning";
    private Double salary = 50000.0;
    private LocalDate joinedDate = LocalDate.now();
    private Address address = null;
    private StaffStatus status = StaffStatus.ACTIVE;
    private Long supervisorId = null;
    private String emergencyContactName = "Bob Smith";
    private String emergencyContactPhone = "5559876543";
    private String profileImageUrl = null;
    private Long userId = 1L;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static StaffTestBuilder aStaff() {
        return new StaffTestBuilder();
    }

    public StaffTestBuilder withId(Long id) { this.id = id; return this; }
    public StaffTestBuilder withFirstName(String firstName) { this.firstName = firstName; return this; }
    public StaffTestBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
    public StaffTestBuilder withEmail(String email) { this.email = email; return this; }
    public StaffTestBuilder withPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
    public StaffTestBuilder withDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
    public StaffTestBuilder withGender(Gender gender) { this.gender = gender; return this; }
    public StaffTestBuilder withEmployeeId(String employeeId) { this.employeeId = employeeId; return this; }
    public StaffTestBuilder withRole(StaffRole role) { this.role = role; return this; }
    public StaffTestBuilder withDepartment(String department) { this.department = department; return this; }
    public StaffTestBuilder withShift(String shift) { this.shift = shift; return this; }
    public StaffTestBuilder withSalary(Double salary) { this.salary = salary; return this; }
    public StaffTestBuilder withJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; return this; }
    public StaffTestBuilder withAddress(Address address) { this.address = address; return this; }
    public StaffTestBuilder withStatus(StaffStatus status) { this.status = status; return this; }
    public StaffTestBuilder withSupervisorId(Long supervisorId) { this.supervisorId = supervisorId; return this; }
    public StaffTestBuilder withEmergencyContactName(String name) { this.emergencyContactName = name; return this; }
    public StaffTestBuilder withEmergencyContactPhone(String phone) { this.emergencyContactPhone = phone; return this; }
    public StaffTestBuilder withProfileImageUrl(String url) { this.profileImageUrl = url; return this; }
    public StaffTestBuilder withUserId(Long userId) { this.userId = userId; return this; }
    public StaffTestBuilder withCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public StaffTestBuilder withUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

    public Staff build() {
        return new Staff(
                id, firstName, lastName, email, phoneNumber, dateOfBirth, gender, employeeId, role,
                department, shift, salary, joinedDate, address, status, supervisorId,
                emergencyContactName, emergencyContactPhone, profileImageUrl, userId, createdAt, updatedAt
        );
    }
}

