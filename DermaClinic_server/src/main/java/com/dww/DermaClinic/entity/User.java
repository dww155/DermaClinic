package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email",  columnNames = "email"),
                @UniqueConstraint(name = "uk_users_phone",  columnNames = "phone_number")
        }
)
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @Column(name = "email", nullable = false, length = 150)
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "first_name", nullable = false, length = 50)
    String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    String lastName;

    @Column(name = "phone_number", length = 15)
    String phoneNumber;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    Gender gender;

    @Column(name = "avatar_url", length = 500)
    String avatarUrl;

    @Column(name = "address", length = 500)
    String address;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    boolean enabled = true;

    @Column(name = "is_email_verified", nullable = false)
    @Builder.Default
    boolean emailVerified = false;

    @Column(name = "auth_provider", nullable = false, length = 20)
    @Builder.Default
    String authProvider = "LOCAL";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    Set<Role> roles = new HashSet<>();
}
