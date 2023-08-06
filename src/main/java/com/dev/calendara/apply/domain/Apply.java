package com.dev.calendara.apply.domain;

import com.dev.calendara.apply.domain.enumeration.ApplyStatus;
import com.dev.calendara.appointment.Appointment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime applyStartTime;

    private LocalDateTime applyEndTime;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Builder
    public Apply(LocalDateTime applyStartTime, LocalDateTime applyEndTime, Long memberId, Appointment appointment, ApplyStatus applyStatus) {
        this.applyStartTime = applyStartTime;
        this.applyEndTime = applyEndTime;
        this.memberId = memberId;
        this.applyStatus = applyStatus;
        addApply(appointment);
    }

    public void addApply(Appointment appointment) {
        if (this.appointment != null) {
            this.appointment.getApplies().remove(this);
        }
        this.appointment = appointment;
        appointment.addApply(this);
    }

    public void changeApplyStatus(ApplyStatus applyStatus) {
        this.applyStatus = applyStatus;
    }
}
