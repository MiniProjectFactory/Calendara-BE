package com.dev.calendara.appointment;

import com.dev.calendara.apply.Apply;
import com.dev.calendara.availabletimes.AvailableTime;
import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long hostId;

    @Column(nullable = false)
    private int meetingDuration;

    @Column(nullable = false)
    private LocalDate meetingStartDate;

    @Column(nullable = false)
    private LocalDate meetingEndDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appointment")
    private final List<AvailableTime> availableTimes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appointment")
    private final List<Apply> applies = new ArrayList<>();

    @Builder
    public Appointment(String title, Long hostId, int meetingDuration, LocalDate meetingStartDate, LocalDate meetingEndDate) {
        validateDateRange(meetingStartDate, meetingEndDate);
        this.title = title;
        this.hostId = hostId;
        this.meetingDuration = meetingDuration;
        this.meetingStartDate = meetingStartDate;
        this.meetingEndDate = meetingEndDate;
    }

    private void validateDateRange(LocalDate meetingStartDate, LocalDate meetingEndDate) {
        if (meetingStartDate.isAfter(meetingEndDate)) {
            throw new BusinessException(ErrorMessage.INVALID_APPOINTMENT_DATE_RANGE);
        }
    }

    public void addAvailableTime(AvailableTime availableTime) {
        availableTimes.add(availableTime);
    }
}
