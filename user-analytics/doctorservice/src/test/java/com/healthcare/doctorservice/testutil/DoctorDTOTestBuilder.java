package com.healthcare.doctorservice.testutil;

import com.healthcare.doctorservice.dto.*;
import com.healthcare.doctorservice.entity.DoctorStatus;
import com.healthcare.doctorservice.entity.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DoctorDTOTestBuilder {
    private Long id = 1L;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "john.doe@example.com";
    private String phoneNumber = "+12345678901";
    private LocalDate dateOfBirth = LocalDate.of(1980, 1, 1);
    private Gender gender = Gender.MALE;
    private String licenseNumber = "LIC123456";
    private String specialization = "Cardiology";
    private List<String> qualifications = List.of("MBBS", "MD");
    private List<String> languages = List.of("English", "Spanish");
    private Integer yearsOfExperience = 10;
    private Double consultationFee = 100.0;
    private String biography = "Experienced cardiologist.";
    private AddressDTO address = null;
    private List<ScheduleDTO> schedules = null;
    private DoctorStatus status = DoctorStatus.ACTIVE;
    private LocalDate joinedDate = LocalDate.of(2020, 1, 1);
    private String profileImageUrl = "http://example.com/image.jpg";
    private String department = "Cardiology";
    private String roomNumber = "101";
    private String emergencyContactName = "Jane Doe";
    private String emergencyContactPhone = "+10987654321";
    private Long userId = 10L;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static DoctorDTOTestBuilder defaultDoctor() {
        return new DoctorDTOTestBuilder();
    }

    public DoctorDTOTestBuilder id(Long id) { this.id = id; return this; }
    public DoctorDTOTestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
    public DoctorDTOTestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
    public DoctorDTOTestBuilder email(String email) { this.email = email; return this; }
    public DoctorDTOTestBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
    public DoctorDTOTestBuilder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
    public DoctorDTOTestBuilder gender(Gender gender) { this.gender = gender; return this; }
    public DoctorDTOTestBuilder licenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; return this; }
    public DoctorDTOTestBuilder specialization(String specialization) { this.specialization = specialization; return this; }
    public DoctorDTOTestBuilder qualifications(List<String> qualifications) { this.qualifications = qualifications; return this; }
    public DoctorDTOTestBuilder languages(List<String> languages) { this.languages = languages; return this; }
    public DoctorDTOTestBuilder yearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; return this; }
    public DoctorDTOTestBuilder consultationFee(Double consultationFee) { this.consultationFee = consultationFee; return this; }
    public DoctorDTOTestBuilder biography(String biography) { this.biography = biography; return this; }
    public DoctorDTOTestBuilder address(AddressDTO address) { this.address = address; return this; }
    public DoctorDTOTestBuilder schedules(List<ScheduleDTO> schedules) { this.schedules = schedules; return this; }
    public DoctorDTOTestBuilder status(DoctorStatus status) { this.status = status; return this; }
    public DoctorDTOTestBuilder joinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; return this; }
    public DoctorDTOTestBuilder profileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; return this; }
    public DoctorDTOTestBuilder department(String department) { this.department = department; return this; }
    public DoctorDTOTestBuilder roomNumber(String roomNumber) { this.roomNumber = roomNumber; return this; }
    public DoctorDTOTestBuilder emergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; return this; }
    public DoctorDTOTestBuilder emergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; return this; }
    public DoctorDTOTestBuilder userId(Long userId) { this.userId = userId; return this; }
    public DoctorDTOTestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public DoctorDTOTestBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

    public DoctorDTO build() {
        return new DoctorDTO(
                id, firstName, lastName, email, phoneNumber, dateOfBirth, gender, licenseNumber,
                specialization, qualifications, languages, yearsOfExperience, consultationFee,
                biography, address, schedules, status, joinedDate, profileImageUrl, department,
                roomNumber, emergencyContactName, emergencyContactPhone, userId, createdAt, updatedAt
        );
    }


}
