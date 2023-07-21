package com.dev.calendara.availabletimes;

import com.dev.calendara.appointment.Appointment;
import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailableTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime availableStartTime;

    private LocalDateTime availableEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Builder
    public AvailableTime(LocalDateTime availableStartTime, LocalDateTime availableEndTime) {
        this.availableStartTime = availableStartTime;
        this.availableEndTime = availableEndTime;
    }

    public void addAvailableTime(Appointment appointment) {
        if (this.appointment != null) {
            this.appointment.getAvailableTimes().remove(this);
        }
        validateAvailableTimes(appointment);
        this.appointment = appointment;
        appointment.addAvailableTime(this);
    }

    private void validateAvailableTimes(Appointment appointment) {
        LocalDate meetingStartDate = appointment.getMeetingStartDate();
        LocalDate meetingEndDate = appointment.getMeetingEndDate();
        if (meetingStartDate.atStartOfDay().isAfter(availableStartTime) || meetingEndDate.atTime(23, 59, 59).isBefore(availableEndTime)) {
            throw new BusinessException(ErrorMessage.INVALID_AVAILABLE_TIME);
        }
    }
}
