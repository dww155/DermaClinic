package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.NotificationChannel;
import com.dww.DermaClinic.enums.NotificationDeliveryStatus;
import com.dww.DermaClinic.enums.NotificationType;
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
@Table(name = "patient_care_notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientCareNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    Long notificationId; // BIGSERIAL PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    TreatmentSession session;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", length = 30)
    NotificationChannel channel; // delivery channel: SMS, ZALO_ZNS, EMAIL, APP_PUSH

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 50)
    NotificationType notificationType;

    @Column(name = "scheduled_send_time", nullable = false)
    Instant scheduledSendTime;

    @Column(name = "sent_at")
    Instant sentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 30)
    @Builder.Default
    NotificationDeliveryStatus deliveryStatus = NotificationDeliveryStatus.DANG_CHO; // default pending

    @Column(name = "message_payload", columnDefinition = "TEXT", nullable = false)
    String messagePayload;

    @Column(name = "patient_response_feedback", columnDefinition = "TEXT")
    String patientResponseFeedback;
}
