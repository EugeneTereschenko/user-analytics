/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.repository;

import com.healthcare.patientservice.PatientServiceApplication;
import com.healthcare.patientservice.entity.Patient;
import com.healthcare.patientservice.entity.PatientStatus;
import com.healthcare.patientservice.testutil.PatientTestBuilder;
import com.healthcare.patientservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {PatientServiceApplication.class})
@Import(TestcontainersConfiguration.class)
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @DisplayName("Should save and find patient by email")
    void testFindByEmail() {
        Patient patient = new PatientTestBuilder()
                .withFirstName("John")
                .withLastName("Doe")
                .withEmail("john.doe@example.com")
                .withStatus(PatientStatus.ACTIVE)
                .build();
        patientRepository.save(patient);

        Optional<Patient> found = patientRepository.findByEmail("john.doe@example.com");
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    @DisplayName("Should check existence by email")
    void testExistsByEmail() {
        Patient patient = new PatientTestBuilder()
                .withFirstName("Jane")
                .withLastName("Smith")
                .withEmail("jane.smith@example.com")
                .withStatus(PatientStatus.ACTIVE)
                .build();
        patientRepository.save(patient);

        assertTrue(patientRepository.existsByEmail("jane.smith@example.com"));
        assertFalse(patientRepository.existsByEmail("not.exists@example.com"));
    }

    @Test
    @DisplayName("Should find patients by status")
    void testFindByStatus() {
        Patient patient = new PatientTestBuilder()
                .withFirstName("Alice")
                .withLastName("Brown")
                .withEmail("alice.brown@example.com")
                .withStatus(PatientStatus.INACTIVE)
                .build();
        patientRepository.save(patient);

        var page = patientRepository.findByStatus(PatientStatus.INACTIVE, PageRequest.of(0, 10));
        assertFalse(page.isEmpty());
    }

    @Test
    @DisplayName("Should search patients by term")
    void testSearchPatients() {
        Patient patient = new PatientTestBuilder()
                .withFirstName("Bob")
                .withLastName("Marley")
                .withEmail("bob.marley@example.com")
                .withStatus(PatientStatus.ACTIVE)
                .build();
        patientRepository.save(patient);

        var page = patientRepository.searchPatients("bob", PageRequest.of(0, 10));
        assertFalse(page.isEmpty());
    }


}