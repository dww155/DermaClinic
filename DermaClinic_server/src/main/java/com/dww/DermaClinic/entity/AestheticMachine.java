package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.MachineMaintenanceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "aesthetic_machines",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_aesthetic_machines_serial", columnNames = "serial_number")
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AestheticMachine {

    /** SERIAL PK → Integer IDENTITY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    Integer machineId;

    /** Clinic that owns this machine */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    Clinic clinic;

    @Column(name = "machine_name", nullable = false, length = 150)
    String machineName;

    /** Laser / IPL / RF etc. */
    @Column(name = "technology_type", length = 100)
    String technologyType;

    @Column(name = "serial_number", unique = true, length = 100)
    String serialNumber;

    /** Current operational status */
    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_status", length = 50)
    MachineMaintenanceStatus maintenanceStatus;
}
