package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_supplies")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalSupply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long itemId; // BIGSERIAL PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    Clinic clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    InventoryCategory category;

    @Column(name = "item_code", unique = true, nullable = false, length = 50)
    String itemCode;

    @Column(name = "item_name", nullable = false)
    String itemName;

    @Column(name = "base_unit", nullable = false, length = 50)
    String baseUnit;

    @Column(name = "cost_per_unit", nullable = false, precision = 15, scale = 2)
    BigDecimal costPerUnit;

    @Column(name = "retail_price", nullable = false, precision = 15, scale = 2)
    BigDecimal retailPrice;

    @Column(name = "reorder_level")
    @Builder.Default
    Integer reorderLevel = 10; // default 10

    @Column(name = "is_prescription_drug", nullable = false)
    @Builder.Default
    Boolean isPrescriptionDrug = false; // default false

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true; // default true
}
