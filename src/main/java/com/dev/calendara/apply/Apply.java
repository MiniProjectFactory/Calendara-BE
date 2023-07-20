package com.dev.calendara.apply;

import com.dev.calendara.appointment.Appointment;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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

    private Long userId;

    private String confirmYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    public void applyAppointment(Appointment appointment) {
        if (this.appointment != null) {
            this.appointment.getApplies().remove(this);
        }
        this.appointment = appointment;
        appointment.getApplies().add(this);
    }
}
