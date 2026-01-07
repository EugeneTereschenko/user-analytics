/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.repository;

import com.example.common.security.client.AuthServiceClient;
import com.healthcare.appointmentservice.AppointmentServiceApplication;
import com.healthcare.appointmentservice.entity.Appointment;
import com.healthcare.appointmentservice.testutil.AppointmentTestBuilder;
import com.healthcare.appointmentservice.testutil.TestcontainersConfiguration;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {AppointmentServiceApplication.class})
@Import({TestcontainersConfiguration.class, FeignAutoConfiguration.class})
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @MockBean
    private AuthServiceClient authServiceClient;

    @Test
    @WithMockUser
    void testSaveAndFindById() {
        Appointment appointment = new AppointmentTestBuilder()
                .withPatientName("John Doe")
                .withAppointmentDateTime(LocalDateTime.now().plusDays(1))
                .build();
        Appointment saved = appointmentRepository.save(appointment);

        Optional<Appointment> found = appointmentRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getPatientName());
    }

    @Test
    @WithMockUser
    void testDelete() {
        Appointment appointment = new AppointmentTestBuilder()
                .withPatientName("Jane Smith")
                .withAppointmentDateTime(LocalDateTime.now().plusDays(2))
                .build();
        Appointment saved = appointmentRepository.save(appointment);

        appointmentRepository.deleteById(saved.getId());
        Optional<Appointment> found = appointmentRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    @WithMockUser
    void testFindAll() {
        Appointment appointment1 = new AppointmentTestBuilder()
                .withPatientName("Alice")
                .withAppointmentDateTime(LocalDateTime.now().plusDays(3))
                .build();
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new AppointmentTestBuilder()
                .withPatientName("Bob")
                .withAppointmentDateTime(LocalDateTime.now().plusDays(4))
                .build();
        appointmentRepository.save(appointment2);

        Iterable<Appointment> all = appointmentRepository.findAll();
        assertTrue(all.iterator().hasNext());
    }
}