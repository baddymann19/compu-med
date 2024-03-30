package com.compu.visit.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class VisitRequestDTO {
    public Long id;

    @NotNull(message="dateTime may not null")
    private LocalDateTime dateTime;

    @NotNull(message="visitType may not null")
    private VisitType visitType;
    @NotBlank(message="reason may not be blank")
    private String reason;
    private String familyHistory;
    @NotNull(message="patientId may not be blank")
    private Long patientId;
}
