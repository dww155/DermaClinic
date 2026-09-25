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
@Table(name = "treatment_consumables_usage")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentConsumablesUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    Long usageId; // BIGSERIAL PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    TreatmentSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    StockBatch stockBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    MedicalSupply medicalSupply;

    @Column(name = "quantity_used", nullable = false, precision = 10, scale = 3)
    BigDecimal quantityUsed;

    @Column(name = "unit_cost_at_time", nullable = false, precision = 15, scale = 2)
    BigDecimal unitCostAtTime;

    // DB-computed STORED column — never inserted or updated by JPA
    @Column(name = "total_cost", insertable = false, updatable = false, precision = 15, scale = 2)
    BigDecimal totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deducted_by_user_id")
    User deductedByUser; // references users(user_id) — UUID

    @Column(name = "deducted_at")
    Instant deductedAt; // set on first persist

    @Column(name = "notes", length = 255)
    String notes;

    @PrePersist
    protected void onCreate() {
        if (this.deductedAt == null) {
            this.deductedAt = Instant.now(); // stamp deduction time on create
        }
    }
}
