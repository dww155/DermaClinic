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
@Table(name = "consent_templates")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    Integer templateId; // SERIAL PK

    @Column(name = "procedure_name", nullable = false)
    String procedureName;

    @Column(name = "template_version", nullable = false, length = 20)
    String templateVersion;

    @Column(name = "legal_terms_content", columnDefinition = "TEXT", nullable = false)
    String legalTermsContent;

    @Column(name = "applicable_technologies", columnDefinition = "TEXT")
    String applicableTechnologies;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true; // default true

    @Column(name = "created_at", updatable = false)
    Instant createdAt; // set on create only

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now(); // stamp creation time
        }
    }
}
