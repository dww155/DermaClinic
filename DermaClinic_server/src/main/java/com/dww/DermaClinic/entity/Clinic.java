package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinics")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinic_id")
    Long clinicId;

    @Column(name = "clinic_name", nullable = false)
    String clinicName;

    @Column(name = "license_number", unique = true, nullable = false, length = 100)
    String licenseNumber;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    String address;

    @Column(name = "phone_number", nullable = false, length = 20)
    String phoneNumber;

    @Column(name = "email", length = 100)
    String email;

    // Stored as JSONB — e.g. {"Mon-Fri":"08:00-17:00"}
    @Column(name = "operating_hours", columnDefinition = "jsonb")
    String operatingHours;

    @Builder.Default
    @Column(name = "is_active")
    Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = Instant.now();
        // updatedAt intentionally left null on create
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
