package com.dww.DermaClinic.entity;

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
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    String name;

    @Column(name = "description", length = 255)
    String description;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
