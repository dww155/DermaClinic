package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_batches")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    Long batchId; // BIGSERIAL PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    MedicalSupply medicalSupply;

    @Column(name = "batch_number", nullable = false, length = 100)
    String batchNumber;

    @Column(name = "expiry_date", nullable = false)
    LocalDate expiryDate;

    @Column(name = "initial_quantity", nullable = false, precision = 12, scale = 3)
    BigDecimal initialQuantity;

    @Column(name = "current_quantity", nullable = false, precision = 12, scale = 3)
    BigDecimal currentQuantity;

    @Column(name = "received_date", nullable = false)
    LocalDate receivedDate;

    @Column(name = "supplier_name")
    String supplierName;
}
