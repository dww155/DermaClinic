package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "active_ingredients")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActiveIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    Integer ingredientId; // SERIAL PK

    @Column(name = "scientific_name", nullable = false)
    String scientificName;

    @Column(name = "standard_unit", nullable = false, length = 50)
    String standardUnit;

    @Column(name = "mechanism_description", columnDefinition = "TEXT")
    String mechanismDescription;
}
