package com.healthcare.patientservice.testutil;

import com.healthcare.patientservice.entity.Gender;
import com.healthcare.patientservice.entity.Patient;
import com.healthcare.patientservice.entity.PatientStatus;

import java.time.LocalDate;

public class PatientTestBuilder {
    private String firstName = "Default";
    private String lastName = "User";
    private String email = "default.user@example.com";
    private String phoneNumber = "1234567890";
    private PatientStatus status = PatientStatus.ACTIVE;
    private LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
    private String gender = "OTHER"; // Set a default value

    public PatientTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PatientTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PatientTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public PatientTestBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PatientTestBuilder withStatus(PatientStatus status) {
        this.status = status;
        return this;
    }

    public PatientTestBuilder withDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public PatientTestBuilder withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Patient build() {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setEmail(email);
        patient.setPhoneNumber(phoneNumber);
        patient.setStatus(status);
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(Gender.FEMALE);
        return patient;
    }
}
