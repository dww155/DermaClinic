package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.ImageStage;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinical_skin_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClinicalSkinImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    Long imageId;

    // Patient who owns this image (DB-level CASCADE DELETE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    // Placeholder FK — TreatmentSession entity linked later
    @Column(name = "treatment_session_id")
    Long treatmentSessionId;

    // Device used to capture the image
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imaging_device_id")
    ImagingDevice imagingDevice;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage", length = 20)
    ImageStage stage;

    @Column(name = "angle", length = 50)
    String angle;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    String imageUrl;

    @Column(name = "image_hash", nullable = false, length = 64)
    String imageHash;

    // AI-generated analysis results stored as JSONB
    @Column(name = "analyzed_metrics", columnDefinition = "jsonb")
    String analyzedMetrics;

    // User (staff) who captured the image; User PK is UUID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captured_by_user_id")
    User capturedByUser;

    @Column(name = "captured_at")
    Instant capturedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    String notes;

    // Set capturedAt on first persist; no updatedAt for images
    @PrePersist
    protected void onCreate() {
        if (this.capturedAt == null) this.capturedAt = Instant.now();
    }
}
