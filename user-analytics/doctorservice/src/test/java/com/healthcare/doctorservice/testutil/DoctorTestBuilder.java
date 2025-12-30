package com.healthcare.doctorservice.testutil;

import com.healthcare.doctorservice.entity.*;

import java.time.LocalDate;
import java.util.Arrays;

public class DoctorTestBuilder {
    private Long id = 1L;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "john.doe@example.com";
    private String phoneNumber = "1234567890";
    private LocalDate dateOfBirth = LocalDate.of(1980, 1, 1);
    private Gender gender = Gender.MALE;
    private String licenseNumber = "LIC123456";
    private String specialization = "Cardiology";
    private java.util.List<String> qualifications = Arrays.asList("MBBS", "MD");
    private java.util.List<String> languages = Arrays.asList("English", "Spanish");
    private Integer yearsOfExperience = 10;
    private Double consultationFee = 100.0;
    private String biography = "Experienced cardiologist.";
    private Address address = null;
    private java.util.List<Schedule> schedules = null;
    private DoctorStatus status = DoctorStatus.ACTIVE;
    private LocalDate joinedDate = LocalDate.now();
    private String profileImageUrl = null;
    private String department = "Cardiology";
    private String roomNumber = "101";
    private String emergencyContactName = "Jane Doe";
    private String emergencyContactPhone = "0987654321";

    public DoctorTestBuilder withId(Long id) { this.id = id; return this; }
    public DoctorTestBuilder withFirstName(String firstName) { this.firstName = firstName; return this; }
    public DoctorTestBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
    public DoctorTestBuilder withEmail(String email) { this.email = email; return this; }
    public DoctorTestBuilder withPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
    public DoctorTestBuilder withDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
    public DoctorTestBuilder withGender(Gender gender) { this.gender = gender; return this; }
    public DoctorTestBuilder withLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; return this; }
    public DoctorTestBuilder withSpecialization(String specialization) { this.specialization = specialization; return this; }
    public DoctorTestBuilder withQualifications(java.util.List<String> qualifications) { this.qualifications = qualifications; return this; }
    public DoctorTestBuilder withLanguages(java.util.List<String> languages) { this.languages = languages; return this; }
    public DoctorTestBuilder withYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; return this; }
    public DoctorTestBuilder withConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; return this; }
    public DoctorTestBuilder withBiography(String biography) { this.biography = biography; return this; }
    public DoctorTestBuilder withAddress(Address address) { this.address = address; return this; }
    public DoctorTestBuilder withSchedules(java.util.List<Schedule> schedules) { this.schedules = schedules; return this; }
    public DoctorTestBuilder withStatus(DoctorStatus status) { this.status = status; return this; }
    public DoctorTestBuilder withJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; return this; }
    public DoctorTestBuilder withProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; return this; }
    public DoctorTestBuilder withDepartment(String department) { this.department = department; return this; }
    public DoctorTestBuilder withRoomNumber(String roomNumber) { this.roomNumber = roomNumber; return this; }
    public DoctorTestBuilder withEmergencyContactName(String name) { this.emergencyContactName = name; return this; }
    public DoctorTestBuilder withEmergencyContactPhone(String phone) { this.emergencyContactPhone = phone; return this; }

    public Doctor build() {
        return new Doctor(
                id, firstName, lastName, email, phoneNumber, dateOfBirth, gender, licenseNumber,
                specialization, qualifications, languages, yearsOfExperience, consultationFee, biography,
                address, schedules, status, joinedDate, profileImageUrl, department, roomNumber,
                emergencyContactName, emergencyContactPhone, null, null
        );
    }

    public static DoctorTestBuilder aDoctor() {
        return new DoctorTestBuilder();
    }

    public static DoctorTestBuilder defaultDoctor() {
        return new DoctorTestBuilder();
    }


}

