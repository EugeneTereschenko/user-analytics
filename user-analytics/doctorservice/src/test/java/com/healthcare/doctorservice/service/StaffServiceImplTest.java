package com.healthcare.doctorservice.service;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.doctorservice.config.WithMockUserPrincipal;
import com.healthcare.doctorservice.dto.StaffDTO;
import com.healthcare.doctorservice.entity.Staff;
import com.healthcare.doctorservice.entity.StaffRole;
import com.healthcare.doctorservice.entity.StaffStatus;
import com.healthcare.doctorservice.exception.StaffAlreadyExistsException;
import com.healthcare.doctorservice.exception.StaffNotFoundException;
import com.healthcare.doctorservice.mapper.StaffMapper;
import com.healthcare.doctorservice.repository.StaffRepository;
import com.healthcare.doctorservice.testutil.StaffTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Staff Service Tests")
class StaffServiceImplTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private StaffMapper staffMapper;

    @InjectMocks
    private StaffServiceImpl staffService;

    private StaffDTO staffDTO;
    private Staff staff;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin", null, List.of())
        );

        staffDTO = new StaffDTO();
        staffDTO.setEmail("staff@health.com");
        staffDTO.setEmployeeId("EMP001");
        staffDTO.setRole(StaffRole.NURSE);
        staffDTO.setStatus(StaffStatus.ACTIVE);

        staff = StaffTestBuilder.aStaff()
                .withEmail("staff@health.com")
                .withEmployeeId("EMP001")
                .withRole(StaffRole.NURSE)
                .withStatus(StaffStatus.ACTIVE)
                .build();
    }

    @Test
    void createStaff_success() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(Optional.of(1L));
            when(staffRepository.existsByEmail(anyString())).thenReturn(false);
            when(staffRepository.existsByEmployeeId(anyString())).thenReturn(false);
            when(staffMapper.toEntity(any(StaffDTO.class))).thenReturn(staff);
            when(staffRepository.save(any(Staff.class))).thenReturn(staff);
            when(staffMapper.toDTO(any(Staff.class))).thenReturn(staffDTO);

            StaffDTO result = staffService.createStaff(staffDTO);

            assertNotNull(result);
            assertEquals(staffDTO.getEmail(), result.getEmail());
            verify(staffRepository).save(any(Staff.class));
        }
    }

    @Test
    void createStaff_emailExists_throwsException() {
        when(staffRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(StaffAlreadyExistsException.class, () -> staffService.createStaff(staffDTO));
    }

    @Test
    void createStaff_employeeIdExists_throwsException() {
        when(staffRepository.existsByEmail(anyString())).thenReturn(false);
        when(staffRepository.existsByEmployeeId(anyString())).thenReturn(true);

        assertThrows(StaffAlreadyExistsException.class, () -> staffService.createStaff(staffDTO));
    }

    @Test
    void getStaffById_success() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(staff));
        when(staffMapper.toDTO(any(Staff.class))).thenReturn(staffDTO);

        StaffDTO result = staffService.getStaffById(1L);

        assertNotNull(result);
        assertEquals(staffDTO.getEmail(), result.getEmail());
    }

    @Test
    void getStaffById_notFound_throwsException() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.getStaffById(1L));
    }

    @Test
    void updateStaff_success() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(staff));
        //when(staffRepository.existsByEmail(anyString())).thenReturn(false);
        doNothing().when(staffMapper).updateEntityFromDTO(any(StaffDTO.class), any(Staff.class));
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);
        when(staffMapper.toDTO(any(Staff.class))).thenReturn(staffDTO);

        StaffDTO result = staffService.updateStaff(1L, staffDTO);

        assertNotNull(result);
        verify(staffRepository).save(any(Staff.class));
    }

    @Test
    void updateStaff_notFound_throwsException() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.updateStaff(1L, staffDTO));
    }

    @Test
    void updateStaff_emailExists_throwsException() {
        Staff otherStaff = StaffTestBuilder.aStaff()
                .withEmail("other@health.com")
                .withEmployeeId("EMP002")
                .build();

        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(otherStaff));
        when(staffRepository.existsByEmail(anyString())).thenReturn(true);

        staffDTO.setEmail("staff@health.com");
        assertThrows(StaffAlreadyExistsException.class, () -> staffService.updateStaff(1L, staffDTO));
    }

    @Test
    void deleteStaff_success() {
        when(staffRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(staffRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> staffService.deleteStaff(1L));
        verify(staffRepository).deleteById(1L);
    }

    @Test
    void deleteStaff_notFound_throwsException() {
        when(staffRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(StaffNotFoundException.class, () -> staffService.deleteStaff(1L));
    }
}
