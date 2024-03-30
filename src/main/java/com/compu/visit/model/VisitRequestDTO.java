package com.compu.visit.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class VisitRequestDTO {
    public Long id;

    private LocalDateTime dateTime;
    private VisitType visitType;
    private String reason;
    private String familyHistory;

    private Long patientId;
}
