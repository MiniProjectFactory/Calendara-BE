package com.dev.calendara.appointment.repository;

import com.dev.calendara.apply.domain.enumeration.ApplyStatus;
import com.dev.calendara.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("select a from Appointment a join fetch a.applies ap where a.id=:appointmentId and a.hostId=:hostId and ap.applyStatus=:applyStatus")
    Optional<Appointment> findByIdAndHostIdAndApplyStatus(Long appointmentId, Long hostId, ApplyStatus applyStatus);
}
