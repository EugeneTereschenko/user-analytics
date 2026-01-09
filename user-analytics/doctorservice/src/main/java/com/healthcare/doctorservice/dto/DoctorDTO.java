/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.doctorservice.dto;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.doctorservice.entity.DoctorStatus;
import com.healthcare.doctorservice.entity.Gender;
import com.healthcare.doctorservice.exception.UnauthorizedException;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid")
    private String phoneNumber;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(max = 50, message = "License number must not exceed 50 characters")
    private String licenseNumber;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    private List<String> qualifications;

    private List<String> languages;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    @Min(value = 0, message = "Consultation fee cannot be negative")
    private Double consultationFee;

    private String biography;

    private AddressDTO address;

    private List<ScheduleDTO> schedules;

    private DoctorStatus status;

    private LocalDate joinedDate;

    private String profileImageUrl;

    private String department;

    private String roomNumber;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void checkUserId() {
        if (this.userId == null) {
            Long currentUserId = SecurityUtils.getCurrentUserId()
                    .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
            this.userId = currentUserId;
        }
    }
}
