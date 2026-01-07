package com.healthcare.doctorservice.repository;

import com.example.common.security.client.AuthServiceClient;
import com.healthcare.doctorservice.DoctorServiceApplication;
import com.healthcare.doctorservice.entity.Doctor;
import com.healthcare.doctorservice.entity.DoctorStatus;
import com.healthcare.doctorservice.testutil.TestcontainersConfiguration;
import com.healthcare.doctorservice.testutil.DoctorTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DoctorServiceApplication.class})
@Import({TestcontainersConfiguration.class, FeignAutoConfiguration.class})
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @MockBean
    private AuthServiceClient authServiceClient;

    @Test
    @WithMockUser
    @DisplayName("Should find doctor by email")
    void shouldFindByEmail() {
        Doctor doctor = DoctorTestBuilder.aDoctor().withEmail("test@example.com").build();
        doctorRepository.save(doctor);

        Optional<Doctor> found = doctorRepository.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @WithMockUser
    @DisplayName("Should check existence by license number")
    void shouldCheckExistsByLicenseNumber() {
        Doctor doctor = DoctorTestBuilder.aDoctor().withLicenseNumber("LIC123").build();
        doctorRepository.save(doctor);

        boolean exists = doctorRepository.existsByLicenseNumber("LIC123");
        assertThat(exists).isTrue();
    }

    @Test
    @WithMockUser
    @DisplayName("Should find all active doctors")
    void shouldFindAllActiveDoctors() {
        Doctor activeDoctor = DoctorTestBuilder.aDoctor()
                .withStatus(DoctorStatus.ACTIVE)
                .withLicenseNumber("LIC123")
                .build();
        Doctor inactiveDoctor = DoctorTestBuilder.aDoctor().withStatus(DoctorStatus.INACTIVE).withEmail("other@example.com").build();
        doctorRepository.save(activeDoctor);
        doctorRepository.save(inactiveDoctor);

        List<Doctor> activeDoctors = doctorRepository.findAllActiveDoctors();
        assertThat(activeDoctors).extracting(Doctor::getStatus).containsOnly(DoctorStatus.ACTIVE);
    }
}
