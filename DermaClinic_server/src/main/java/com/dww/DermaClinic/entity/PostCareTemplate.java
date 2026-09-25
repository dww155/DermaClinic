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
@Table(name = "post_care_templates")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCareTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_template_id")
    Integer careTemplateId; // SERIAL PK

    @Column(name = "procedure_name", nullable = false)
    String procedureName;

    @Column(name = "instructions", columnDefinition = "TEXT", nullable = false)
    String instructions;

    @Column(name = "warning_symptoms", columnDefinition = "TEXT", nullable = false)
    String warningSymptoms;
}
