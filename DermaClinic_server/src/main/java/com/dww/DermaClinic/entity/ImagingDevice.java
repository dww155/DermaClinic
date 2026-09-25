package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imaging_devices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImagingDevice {

    // SERIAL → Integer PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    Integer deviceId;

    @Column(name = "device_name", nullable = false, length = 150)
    String deviceName;

    @Column(name = "model", length = 100)
    String model;

    @Column(name = "serial_number", unique = true, length = 100)
    String serialNumber;

    @Column(name = "calibration_date")
    LocalDate calibrationDate;
}
