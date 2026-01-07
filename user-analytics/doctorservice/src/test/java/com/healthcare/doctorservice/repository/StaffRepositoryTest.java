package com.healthcare.doctorservice.repository;

import com.example.common.security.client.AuthServiceClient;
import com.healthcare.doctorservice.DoctorServiceApplication;
import com.healthcare.doctorservice.entity.*;
import com.healthcare.doctorservice.testutil.StaffTestBuilder;
import com.healthcare.doctorservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DoctorServiceApplication.class})
@Import({TestcontainersConfiguration.class, FeignAutoConfiguration.class})
class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private AuthServiceClient authServiceClient;

    @Test
    @DisplayName("Should save and find staff by email")
    void testFindByEmail() {
        Staff staff = StaffTestBuilder.aStaff().withEmail("unique@email.com").build();
        staffRepository.save(staff);

        Optional<Staff> found = staffRepository.findByEmail("unique@email.com");
        assertTrue(found.isPresent());
        assertEquals("unique@email.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Should check existence by employeeId")
    void testExistsByEmployeeId() {
        Staff staff = StaffTestBuilder.aStaff().withEmployeeId("EMP999").build();
        staffRepository.save(staff);

        assertTrue(staffRepository.existsByEmployeeId("EMP999"));
        assertFalse(staffRepository.existsByEmployeeId("EMP_NOT_EXIST"));
    }

/*    @Test
    @DisplayName("Should find staff by role with paging")
    void testFindByRole() {
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique1@example.com")
                .withRole(StaffRole.NURSE).build());
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique2@example.com")
                .withRole(StaffRole.LAB_TECHNICIAN)
                .withEmployeeId("EMP002").build());

        entityManager.flush();
        entityManager.clear(); // Add this!

        var page = staffRepository.findByRole(StaffRole.NURSE, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals(StaffRole.NURSE, page.getContent().get(0).getRole());
    }*/

    @Test
    @DisplayName("Should find all active staff")
    void testFindAllActiveStaff() {
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique1@example.com")
                .withStatus(StaffStatus.ACTIVE).build());
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique2@example.com")
                .withStatus(StaffStatus.INACTIVE).withEmployeeId("EMP003").build());

        List<Staff> active = staffRepository.findAllActiveStaff();
        assertTrue(active.stream().allMatch(s -> s.getStatus() == StaffStatus.ACTIVE));
    }

/*    @Test
    @DisplayName("Should search staff by term")
    void testSearchStaff() {
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique1@example.com")
                .withFirstName("Anna")
                .withLastName("Smith")
                .withDepartment("Cardiology")
                .withEmployeeId("EMP004").build());
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique2@example.com")
                .withFirstName("Bob")
                .withLastName("Jones")
                .withDepartment("Neurology")
                .withEmployeeId("EMP005").build());

        entityManager.flush();
        entityManager.clear(); // Add this line!

        var page = staffRepository.searchStaff("Anna", PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals("Anna", page.getContent().get(0).getFirstName());
    }*/



/*    @Test
    @DisplayName("Should count active staff by role")
    void testCountActiveByRole() {
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique1@example.com")
                .withRole(StaffRole.NURSE).withStatus(StaffStatus.ACTIVE).build());
        staffRepository.save(StaffTestBuilder.aStaff()
                .withEmail("unique2@example.com")
                .withRole(StaffRole.NURSE).withStatus(StaffStatus.INACTIVE).withEmployeeId("EMP006").build());

        Long count = staffRepository.countActiveByRole(StaffRole.NURSE);
        assertEquals(1L, count);
    }*/
}
