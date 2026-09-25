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
@Table(name = "patient_consent_forms")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientConsentForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_id")
    Long consentId; // BIGSERIAL PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    ConsentTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    TreatmentSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "witness_doctor_id")
    User witnessDoctor; // references users(user_id) — UUID

    @Column(name = "patient_signature_data", columnDefinition = "TEXT", nullable = false)
    String patientSignatureData;

    @Column(name = "patient_signature_hash", nullable = false, length = 64)
    String patientSignatureHash;

    @Column(name = "digital_certificate_id", length = 100)
    String digitalCertificateId;

    @Column(name = "ip_address", length = 45)
    String ipAddress;

    @Column(name = "signed_device_info", columnDefinition = "TEXT")
    String signedDeviceInfo;

    @Column(name = "signed_at", updatable = false)
    Instant signedAt; // set on create only

    @Column(name = "pdf_contract_snapshot_url", columnDefinition = "TEXT", nullable = false)
    String pdfContractSnapshotUrl;

    @PrePersist
    protected void onCreate() {
        if (this.signedAt == null) {
            this.signedAt = Instant.now(); // stamp signing time on create
        }
    }
}
