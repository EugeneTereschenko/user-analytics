/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.repository;

import com.healthcare.appointmentservice.entity.Appointment;
import com.healthcare.appointmentservice.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);

    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId AND a.status = :status")
    List<Appointment> findByPatientIdAndStatus(
            @Param("patientId") Long patientId,
            @Param("status") AppointmentStatus status
    );

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.status = :status")
    List<Appointment> findByDoctorIdAndStatus(
            @Param("doctorId") Long doctorId,
            @Param("status") AppointmentStatus status
    );

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate")
    List<Appointment> findAppointmentsBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId " +
            "AND a.appointmentDateTime BETWEEN :startDate AND :endDate " +
            "AND a.status NOT IN ('CANCELLED', 'NO_SHOW')")
    List<Appointment> findDoctorAppointmentsInRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId " +
            "AND a.appointmentDateTime BETWEEN :startDate AND :endDate " +
            "AND a.status NOT IN ('CANCELLED', 'NO_SHOW')")
    List<Appointment> findPatientAppointmentsInRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctorId = :doctorId " +
            "AND a.appointmentDateTime < :endTime " +
            "AND FUNCTION('DATEADD', MINUTE, a.durationMinutes, a.appointmentDateTime) > :startTime " +
            "AND a.status NOT IN ('CANCELLED', 'NO_SHOW')")
    boolean existsDoctorConflict(
            @Param("doctorId") Long doctorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT a FROM Appointment a WHERE " +
            "a.appointmentDateTime >= :today AND a.appointmentDateTime < :tomorrow " +
            "AND a.status IN ('SCHEDULED', 'CONFIRMED')")
    List<Appointment> findTodaysAppointments(
            @Param("today") LocalDateTime today,
            @Param("tomorrow") LocalDateTime tomorrow
    );
}
