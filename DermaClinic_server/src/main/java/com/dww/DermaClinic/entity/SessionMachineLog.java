package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "session_machine_logs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionMachineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    Long logId;

    /** Session this log belongs to — cascade delete handled at DB level */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    TreatmentSession treatmentSession;

    /** Machine used during this session */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    AestheticMachine aestheticMachine;

    /** Handpiece / applicator type used */
    @Column(name = "handpiece_type", length = 100)
    String handpieceType;

    /** Laser wavelength in nanometres */
    @Column(name = "wavelength_nm")
    Integer wavelengthNm;

    /** Fluence (energy density) in J/cm² */
    @Column(name = "fluence_j_cm2", precision = 8, scale = 2)
    BigDecimal fluenceJCm2;

    /** Pulse duration in milliseconds */
    @Column(name = "pulse_width_ms", precision = 8, scale = 2)
    BigDecimal pulseWidthMs;

    /** Spot size in millimetres */
    @Column(name = "spot_size_mm", precision = 8, scale = 2)
    BigDecimal spotSizeMm;

    /** Repetition frequency in Hz */
    @Column(name = "frequency_hz", precision = 8, scale = 2)
    BigDecimal frequencyHz;

    /** Total laser shots delivered */
    @Column(name = "total_shots_fired")
    Integer totalShotsFired;

    /** Body area targeted */
    @Column(name = "target_area", length = 100)
    String targetArea;

    /** Cooling mechanism applied */
    @Column(name = "cooling_mode", length = 100)
    String coolingMode;

    /** Timestamp set automatically at persist — no update */
    @Column(name = "recorded_at", updatable = false)
    Instant recordedAt;

    @PrePersist
    void prePersist() {
        if (recordedAt == null) recordedAt = Instant.now(); // stamp log creation time
    }
}
