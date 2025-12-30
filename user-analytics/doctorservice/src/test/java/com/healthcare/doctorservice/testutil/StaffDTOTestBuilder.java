package com.healthcare.doctorservice.testutil;

import com.healthcare.doctorservice.dto.AddressDTO;
import com.healthcare.doctorservice.dto.StaffDTO;
import com.healthcare.doctorservice.entity.Gender;
import com.healthcare.doctorservice.entity.StaffRole;
import com.healthcare.doctorservice.entity.StaffStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StaffDTOTestBuilder {
    private Long id = 1L;
    private String firstName = "Alice";
    private String lastName = "Smith";
    private String email = "alice.smith@example.com";
    private String phoneNumber = "+12345678901";
    private LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
    private Gender gender = Gender.FEMALE;
    private String employeeId = "EMP1001";
    private StaffRole role = StaffRole.NURSE;
    private String department = "General";
    private String shift = "Morning";
    private Double salary = 3000.0;
    private LocalDate joinedDate = LocalDate.of(2022, 1, 1);
    private AddressDTO address = null;
    private StaffStatus status = StaffStatus.ACTIVE;
    private Long supervisorId = 2L;
    private String emergencyContactName = "Bob Smith";
    private String emergencyContactPhone = "+10987654321";
    private String profileImageUrl = "http://example.com/profile.jpg";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static StaffDTOTestBuilder defaultStaff() {
        return new StaffDTOTestBuilder();
    }

    public StaffDTOTestBuilder id(Long id) { this.id = id; return this; }
    public StaffDTOTestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
    public StaffDTOTestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
    public StaffDTOTestBuilder email(String email) { this.email = email; return this; }
    public StaffDTOTestBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
    public StaffDTOTestBuilder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
    public StaffDTOTestBuilder gender(Gender gender) { this.gender = gender; return this; }
    public StaffDTOTestBuilder employeeId(String employeeId) { this.employeeId = employeeId; return this; }
    public StaffDTOTestBuilder role(StaffRole role) { this.role = role; return this; }
    public StaffDTOTestBuilder department(String department) { this.department = department; return this; }
    public StaffDTOTestBuilder shift(String shift) { this.shift = shift; return this; }
    public StaffDTOTestBuilder salary(Double salary) { this.salary = salary; return this; }
    public StaffDTOTestBuilder joinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; return this; }
    public StaffDTOTestBuilder address(AddressDTO address) { this.address = address; return this; }
    public StaffDTOTestBuilder status(StaffStatus status) { this.status = status; return this; }
    public StaffDTOTestBuilder supervisorId(Long supervisorId) { this.supervisorId = supervisorId; return this; }
    public StaffDTOTestBuilder emergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; return this; }
    public StaffDTOTestBuilder emergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; return this; }
    public StaffDTOTestBuilder profileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; return this; }
    public StaffDTOTestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public StaffDTOTestBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

    public StaffDTO build() {
        return new StaffDTO(
                id, firstName, lastName, email, phoneNumber, dateOfBirth, gender, employeeId,
                role, department, shift, salary, joinedDate, address, status, supervisorId,
                emergencyContactName, emergencyContactPhone, profileImageUrl, createdAt, updatedAt
        );
    }
}
