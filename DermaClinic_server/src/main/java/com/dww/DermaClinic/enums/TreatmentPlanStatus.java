package com.dww.DermaClinic.enums;

/** Status of a treatment plan lifecycle */
public enum TreatmentPlanStatus {
    LAP_KE_HOACH,   // Planning
    DANG_DIEU_TRI,  // In treatment
    TAM_DUNG,       // Paused
    HOAN_THANH,     // Completed
    HUY             // Cancelled
}
