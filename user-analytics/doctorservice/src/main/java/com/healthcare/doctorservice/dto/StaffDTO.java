/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.doctorservice.dto;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.doctorservice.entity.Gender;
import com.healthcare.doctorservice.entity.StaffRole;
import com.healthcare.doctorservice.entity.StaffStatus;
import com.healthcare.doctorservice.exception.UnauthorizedException;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {

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

    private String employeeId;

    @NotNull(message = "Role is required")
    private StaffRole role;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    private String shift;

    @Min(value = 0, message = "Salary cannot be negative")
    private Double salary;

    private LocalDate joinedDate;

    private AddressDTO address;

    private StaffStatus status;

    private Long supervisorId;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String profileImageUrl;

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
