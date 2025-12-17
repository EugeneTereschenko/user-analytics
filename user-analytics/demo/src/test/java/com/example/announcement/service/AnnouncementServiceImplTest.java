/*
 * © 2024 Yevhen Tereshchenko
 * All rights reserved.
 */

package com.example.announcement.service;

import com.example.announcement.dto.AnnouncementDTO;
import com.example.announcement.model.Announcement;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import com.example.announcement.model.UserAnnouncementRead;
import com.example.announcement.repository.AnnouncementRepository;
import com.example.announcement.repository.UserAnnouncementReadRepository;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Announcement Service Tests")
class AnnouncementServiceImplTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private UserAnnouncementReadRepository readRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    private Announcement announcement;
    private AnnouncementDTO announcementDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        announcement = Announcement.builder()
                .id(1L)
                .title("Test Announcement")
                .body("Test body")
                .date(new Date())
                .author("Test Author")
                .priority(AnnouncementPriority.MEDIUM)
                .category(AnnouncementCategory.GENERAL)
                .isActive(true)
                .build();

        announcementDTO = AnnouncementDTO.builder()
                .id(1L)
                .title("Test Announcement")
                .body("Test body")
                .date("2024-12-16T10:00:00")
                .author("Test Author")
                .priority(AnnouncementPriority.MEDIUM)
                .category(AnnouncementCategory.GENERAL)
                .build();
    }

    @Test
    @DisplayName("getAllAnnouncements - Should return all announcements")
    void getAllAnnouncements_shouldReturnAllAnnouncements() {
        // Given
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findAllByOrderByDateDesc())
                .thenReturn(Arrays.asList(announcement));
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        List<AnnouncementDTO> result = announcementService.getAllAnnouncements();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Announcement");
        assertThat(result.get(0).getIsRead()).isFalse();
        verify(announcementRepository).findAllByOrderByDateDesc();
    }

    @Test
    @DisplayName("getAllAnnouncements - Should return empty list when no announcements")
    void getAllAnnouncements_noAnnouncements_shouldReturnEmptyList() {
        // Given
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findAllByOrderByDateDesc())
                .thenReturn(Collections.emptyList());

        // When
        List<AnnouncementDTO> result = announcementService.getAllAnnouncements();

        // Then
        assertThat(result).isEmpty();
        verify(announcementRepository).findAllByOrderByDateDesc();
    }

    @Test
    @DisplayName("getAnnouncementById - Should return announcement when exists")
    void getAnnouncementById_existingId_shouldReturnAnnouncement() {
        // Given
        Long announcementId = 1L;
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.of(announcement));
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        AnnouncementDTO result = announcementService.getAnnouncementById(announcementId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(announcementId);
        assertThat(result.getTitle()).isEqualTo("Test Announcement");
        verify(announcementRepository).findById(announcementId);
    }

    @Test
    @DisplayName("getAnnouncementById - Should throw exception when not found")
    void getAnnouncementById_nonExistentId_shouldThrowException() {
        // Given
        Long nonExistentId = 999L;
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> announcementService.getAnnouncementById(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Announcement not found");
    }

    @Test
    @DisplayName("getAnnouncementsByPriority - Should return filtered announcements")
    void getAnnouncementsByPriority_shouldReturnFilteredResults() {
        // Given
        AnnouncementPriority priority = AnnouncementPriority.URGENT;
        Announcement urgentAnnouncement = Announcement.builder()
                .id(2L)
                .title("Urgent")
                .body("Urgent body")
                .date(new Date())
                .priority(AnnouncementPriority.URGENT)
                .category(AnnouncementCategory.SECURITY)
                .build();

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findByPriorityOrderByDateDesc(priority))
                .thenReturn(Arrays.asList(urgentAnnouncement));
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        List<AnnouncementDTO> result = announcementService.getAnnouncementsByPriority(priority);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriority()).isEqualTo(AnnouncementPriority.URGENT);
        verify(announcementRepository).findByPriorityOrderByDateDesc(priority);
    }

    @Test
    @DisplayName("getAnnouncementsByCategory - Should return filtered announcements")
    void getAnnouncementsByCategory_shouldReturnFilteredResults() {
        // Given
        AnnouncementCategory category = AnnouncementCategory.MAINTENANCE;
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findByCategory(category))
                .thenReturn(Arrays.asList(announcement));
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        List<AnnouncementDTO> result = announcementService.getAnnouncementsByCategory(category);

        // Then
        assertThat(result).hasSize(1);
        verify(announcementRepository).findByCategory(category);
    }

    @Test
    @DisplayName("getActiveAnnouncements - Should return only active announcements")
    void getActiveAnnouncements_shouldReturnActiveOnly() {
        // Given
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findActiveNonExpired(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(announcement));
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        List<AnnouncementDTO> result = announcementService.getActiveAnnouncements();

        // Then
        assertThat(result).hasSize(1);
        verify(announcementRepository).findActiveNonExpired(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("getUnreadAnnouncements - Should return only unread announcements")
    void getUnreadAnnouncements_shouldReturnUnreadOnly() {
        // Given
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findAllByOrderByDateDesc())
                .thenReturn(Arrays.asList(announcement));
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), announcement.getId()))
                .thenReturn(false);

        // When
        List<AnnouncementDTO> result = announcementService.getUnreadAnnouncements();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsRead()).isFalse();
    }

    @Test
    @DisplayName("getUnreadAnnouncements - Should filter out read announcements")
    void getUnreadAnnouncements_shouldFilterReadAnnouncements() {
        // Given
        Announcement readAnnouncement = Announcement.builder()
                .id(2L)
                .title("Read Announcement")
                .body("Body")
                .date(new Date())
                .priority(AnnouncementPriority.LOW)
                .category(AnnouncementCategory.GENERAL)
                .build();

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findAllByOrderByDateDesc())
                .thenReturn(Arrays.asList(announcement, readAnnouncement));
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), announcement.getId()))
                .thenReturn(false);
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), readAnnouncement.getId()))
                .thenReturn(true);

        // When
        List<AnnouncementDTO> result = announcementService.getUnreadAnnouncements();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(announcement.getId());
    }

    @Test
    @DisplayName("searchAnnouncements - Should return matching announcements")
    void searchAnnouncements_shouldReturnMatchingResults() {
        // Given
        String query = "test";
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.searchAnnouncements(query))
                .thenReturn(Arrays.asList(announcement));
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        List<AnnouncementDTO> result = announcementService.searchAnnouncements(query);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).containsIgnoringCase("test");
        verify(announcementRepository).searchAnnouncements(query);
    }

    @Test
    @DisplayName("createAnnouncement - Should create and return new announcement")
    void createAnnouncement_validData_shouldCreateAnnouncement() {
        // Given
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.save(any(Announcement.class)))
                .thenReturn(announcement);
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        AnnouncementDTO result = announcementService.createAnnouncement(announcementDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(announcementDTO.getTitle());
        verify(announcementRepository).save(any(Announcement.class));
    }

    @Test
    @DisplayName("updateAnnouncement - Should update existing announcement")
    void updateAnnouncement_existingId_shouldUpdateAnnouncement() {
        // Given
        Long announcementId = 1L;
        AnnouncementDTO updateDTO = AnnouncementDTO.builder()
                .title("Updated Title")
                .body("Updated body")
                .date("2024-12-16T10:00:00")
                .priority(AnnouncementPriority.HIGH)
                .category(AnnouncementCategory.FEATURE)
                .build();

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.of(announcement));
        when(announcementRepository.save(any(Announcement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        AnnouncementDTO result = announcementService.updateAnnouncement(announcementId, updateDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        verify(announcementRepository).findById(announcementId);
        verify(announcementRepository).save(any(Announcement.class));
    }

    @Test
    @DisplayName("updateAnnouncement - Should throw exception when not found")
    void updateAnnouncement_nonExistentId_shouldThrowException() {
        // Given
        Long nonExistentId = 999L;
        when(announcementRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() ->
                announcementService.updateAnnouncement(nonExistentId, announcementDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Announcement not found");
    }

    @Test
    @DisplayName("deleteAnnouncement - Should delete announcement")
    void deleteAnnouncement_existingId_shouldDelete() {
        // Given
        Long announcementId = 1L;
        doNothing().when(announcementRepository).deleteById(announcementId);

        // When
        announcementService.deleteAnnouncement(announcementId);

        // Then
        verify(announcementRepository).deleteById(announcementId);
    }

    @Test
    @DisplayName("markAsRead - Should mark announcement as read for user")
    void markAsRead_unreadAnnouncement_shouldMarkAsRead() {
        // Given
        Long announcementId = 1L;
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), announcementId))
                .thenReturn(false);
        when(readRepository.save(any(UserAnnouncementRead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        announcementService.markAsRead(announcementId);

        // Then
        verify(readRepository).existsByUserIdAndAnnouncementId(testUser.getId(), announcementId);
        verify(readRepository).save(any(UserAnnouncementRead.class));
    }

    @Test
    @DisplayName("markAsRead - Should not duplicate read record")
    void markAsRead_alreadyRead_shouldNotDuplicate() {
        // Given
        Long announcementId = 1L;
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), announcementId))
                .thenReturn(true);

        // When
        announcementService.markAsRead(announcementId);

        // Then
        verify(readRepository).existsByUserIdAndAnnouncementId(testUser.getId(), announcementId);
        verify(readRepository, never()).save(any(UserAnnouncementRead.class));
    }

    @Test
    @DisplayName("markAllAsRead - Should mark all announcements as read")
    void markAllAsRead_shouldMarkAllUnreadAsRead() {
        // Given
        Announcement announcement2 = Announcement.builder()
                .id(2L)
                .title("Second")
                .body("Body")
                .date(new Date())
                .priority(AnnouncementPriority.LOW)
                .category(AnnouncementCategory.GENERAL)
                .build();

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findAll())
                .thenReturn(Arrays.asList(announcement, announcement2));
        when(readRepository.existsByUserIdAndAnnouncementId(eq(testUser.getId()), anyLong()))
                .thenReturn(false);
        when(readRepository.save(any(UserAnnouncementRead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        announcementService.markAllAsRead();

        // Then
        verify(announcementRepository).findAll();
        verify(readRepository, times(2)).save(any(UserAnnouncementRead.class));
    }

    @Test
    @DisplayName("markAllAsRead - Should skip already read announcements")
    void markAllAsRead_someAlreadyRead_shouldSkipRead() {
        // Given
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findAll())
                .thenReturn(Arrays.asList(announcement));
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), announcement.getId()))
                .thenReturn(true);

        // When
        announcementService.markAllAsRead();

        // Then
        verify(readRepository, never()).save(any(UserAnnouncementRead.class));
    }

    @Test
    @DisplayName("getAllAnnouncements - Should correctly map isRead status")
    void getAllAnnouncements_shouldMapReadStatusCorrectly() {
        // Given
        Announcement readAnnouncement = Announcement.builder()
                .id(2L)
                .title("Read")
                .body("Body")
                .date(new Date())
                .priority(AnnouncementPriority.LOW)
                .category(AnnouncementCategory.GENERAL)
                .build();

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.findAllByOrderByDateDesc())
                .thenReturn(Arrays.asList(announcement, readAnnouncement));
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), announcement.getId()))
                .thenReturn(false);
        when(readRepository.existsByUserIdAndAnnouncementId(testUser.getId(), readAnnouncement.getId()))
                .thenReturn(true);

        // When
        List<AnnouncementDTO> result = announcementService.getAllAnnouncements();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIsRead()).isFalse();
        assertThat(result.get(1).getIsRead()).isTrue();
    }

    @Test
    @DisplayName("createAnnouncement - Should handle date parsing")
    void createAnnouncement_shouldParseDateCorrectly() {
        // Given
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
        when(announcementRepository.save(any(Announcement.class)))
                .thenReturn(announcement);
        when(readRepository.existsByUserIdAndAnnouncementId(anyLong(), anyLong()))
                .thenReturn(false);

        // When
        AnnouncementDTO result = announcementService.createAnnouncement(announcementDTO);

        // Then
        assertThat(result).isNotNull();
        verify(announcementRepository).save(any(Announcement.class));
    }
}