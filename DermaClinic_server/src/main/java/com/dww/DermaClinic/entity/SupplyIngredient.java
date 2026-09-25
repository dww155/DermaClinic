package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supply_ingredients")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplyIngredient {

    @EmbeddedId
    SupplyIngredientId id; // composite PK

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    MedicalSupply medicalSupply;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    ActiveIngredient activeIngredient;

    @Column(name = "concentration_ratio", precision = 10, scale = 4)
    BigDecimal concentrationRatio;

    // ── Composite PK ──────────────────────────────────────────────────────────
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SupplyIngredientId implements Serializable {

        @Column(name = "item_id")
        Long itemId;

        @Column(name = "ingredient_id")
        Integer ingredientId;
    }
}
