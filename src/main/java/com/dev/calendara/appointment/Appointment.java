package com.dev.calendara.appointment;

import com.dev.calendara.apply.Apply;
import com.dev.calendara.availabletimes.AvailableTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long hostId;

    private LocalTime meetingDuration;

    private LocalDate meetingStartDate;

    private LocalDate meetingEndDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appointment")
    private List<AvailableTime> AvailableTimes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appointment")
    private List<Apply> applies = new ArrayList<>();
}
